import paho.mqtt.publish as publish
from random import random
from random import seed
import time
import pandas as pd

df = pd.read_csv("data/temperature.csv")

for _, row in df.iterrows():
    publish.single("Temperature",
    str(row["Temperature"].item()),
    hostname="host.docker.internal")
    time.sleep(10)
