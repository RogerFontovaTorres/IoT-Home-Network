import paho.mqtt.publish as publish
from random import random
from random import seed
import time
from datetime import datetime
import pandas as pd
import json

df = pd.read_csv("data/temperature.csv")
docker_id = open("/etc/hostname","r").read().split()[0]



for _, row in df.iterrows():   
    temperature = row["Temperature"].item()
    timestamp = round(time.time()*1000)
    message = json.dumps({"sensor_id": docker_id, "temperature": temperature, "timestamp": timestamp}).encode('utf-8')
    print(message)
    publish.single("Temperature",
        message,
        hostname="host.docker.internal")
    time.sleep(5)
