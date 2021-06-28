import tensorflow as tf
import os
import sys

import tokenization
from keras.models import load_model

import numpy as np
import pandas as pd
import tensorflow_hub as hub
from transformers import *
from confluent_kafka import Producer, Consumer, KafkaError
import json

tokenizer = tokenization.FullTokenizer("/home/cms/바탕화면/HanBert-54kN/vocab_54k.txt", use_moran=True)

cls_token_id = 3
sep_token_id = 4
MAX_SEQUENCE_LENGTH = 140

def convert_to_inputs(text, tokenizer,max_seq_len):
    def return_id(str1,length):
        token = tokenizer.tokenize(text)
        if len(token) > max_seq_len-2:
            token = token[:max_seq_len-2]

        inputs_ids = tokenizer.convert_tokens_to_ids(token)    
        inputs_ids = [cls_token_id] + inputs_ids + [sep_token_id]
        input_mask = [1] * len(inputs_ids) #attention_mask

        padding_length = length - len(inputs_ids)
        inputs_ids = inputs_ids + ([0] * padding_length)
        input_mask = input_mask + ([0] * padding_length)
        segment_ids = [0] * len(inputs_ids) #token_type_ids
        return [inputs_ids, input_mask, segment_ids]

    input_ids, input_masks, input_segments = return_id(text, max_seq_len)

    return [input_ids, input_masks, input_segments]

def compute_outputs(df):
    return np.asarray(df['label'])
    
def compute_inputs(df, tokenizer, max_sequence_length):
    input_ids, segment_ids, input_mask = [], [], []
    for document in df['document']:
        inp_ids, inp_mask, seg_ids = convert_to_inputs(document, tokenizer, max_sequence_length)

        input_ids.append(inp_ids)
        segment_ids.append(seg_ids)
        input_mask.append(inp_mask)
    return [np.asarray(input_ids, dtype=np.int32),
            np.asarray(input_mask, dtype=np.int32), 
            np.asarray(segment_ids, dtype=np.int32)]

def create_model():
    ids = tf.keras.layers.Input((MAX_SEQUENCE_LENGTH,), dtype=tf.int32)
    mask = tf.keras.layers.Input((MAX_SEQUENCE_LENGTH,), dtype=tf.int32)
    seg = tf.keras.layers.Input((MAX_SEQUENCE_LENGTH,), dtype=tf.int32)
    
    
    config = BertConfig.from_json_file('/home/cms/바탕화면/HanBert-54kN/bert_config.json')
    config.output_hidden_states = False
    bert_model = TFBertModel.from_pretrained('/home/cms/바탕화면/HanBert-54kN-torch', config=config, from_pt=True)

    x = bert_model([ids, mask, seg])[0]

    
    x = tf.keras.layers.GlobalAveragePooling1D()(x)
    x = tf.keras.layers.Dropout(0.2)(x)
    x = tf.keras.layers.Dense(1, activation='sigmoid')(x)

    model = tf.keras.models.Model(inputs=[ids, mask, seg,], outputs=x)
    
    return model

def customLoadModel(model):
    
    optimizer = tf.keras.optimizers.Adam(learning_rate=1e-5)
    model.compile(loss='binary_crossentropy', optimizer=optimizer,metrics=['accuracy'])
    model.load_weights('/home/cms/바탕화면/HanBert-54kN/bert_model_nsmc_011.h5')

    return model
 
def predict(text):
    model = create_model()
    loaded_model = customLoadModel(model)

    test = pd.DataFrame({'document' : [text]})
    test_inputs = compute_inputs(test, tokenizer, MAX_SEQUENCE_LENGTH)

    prediction = loaded_model.predict(test_inputs)
    prob = (prediction * 100) if prediction >= 0.5 else ((1 - prediction) * 100)

    # 예측 값을 1차원 배열로부터 확인 가능한 문자열로 변환
    label = 'positive' if prediction >= 0.4 else 'negative'
    return label
        
        
        


            
if __name__ == '__main__':
    # topic, broker list
    consumer = Consumer({
        'bootstrap.servers': 'localhost:9092',
        'auto.offset.reset': 'latest',
        'group.id':'myGroup'
        })

    consumer.subscribe(['myCommentTopic'])
    
    while True:
        msg = consumer.poll(0.1)
 	
        if msg is None:
            continue
        if msg.error():
            print( msg.error() )
            continue
            
        print("\n\nget message!")
        message = msg.value().decode('utf-8')
        comment = json.loads(message)
        label = predict(comment.get('content'))
        
        
        
        if label == 'negative':
            comment['cleanCommentYn'] = "false"
            producer = Producer({
                'bootstrap.servers': 'localhost:9093',
                'group.id':'predictGroup'
            })
            producer.poll(0)
            producer.produce("predictTopic", json.dumps(comment))
            
            print(comment)
            producer.flush()
    	
        print( message )
    consumer.close()


