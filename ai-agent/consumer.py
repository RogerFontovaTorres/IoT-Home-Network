from pickle import load, dumps
from mapper.object_mapper_exception import ObjectMapperException
from mapper.object_mapper import ObjectMapper
import json

from kafka import KafkaConsumer, KafkaProducer
import pandas as pd
from prophet import Prophet
import datetime

my_consumer = KafkaConsumer(
    'temperature',
    bootstrap_servers=['localhost : 9092'],
    auto_offset_reset='latest',
    enable_auto_commit=True,
    group_id='my-group',
    value_deserializer=lambda x: json.loads(x)
)
my_producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda x: json.dumps(x).encode('utf-8')
    )
m = load(open("model_trained.pkl", "rb"))
#with open("model_trained.pkl", "rb") as m:

print("starting")
for message in my_consumer:
    print(f"{message} is being processed")
    message = message.value

    
    java_timestamp = message['time']
    seconds = java_timestamp / 1000
    sub_seconds  = (java_timestamp % 1000.0) / 1000.0
    date = datetime.datetime.fromtimestamp(seconds + sub_seconds)

    print(message['time'], date.timestamp()*1000)

    df_pred = pd.DataFrame.from_records([{"ds": date}])
    forecast = m.predict(df_pred)
    forecast['sensorId'] = message['sensorId']
    forecast['ds'] = message['time']
    data_to_send = forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper', 'sensorId']].to_dict(orient="records")
    print(json.dumps(data_to_send[0]))
    my_producer.send('analytics_results',
                     value=data_to_send[0])
    print(forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper', 'sensorId']])
