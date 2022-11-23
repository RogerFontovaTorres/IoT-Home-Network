import paho.mqtt.publish as publish
from random import random
from random import seed
import time
import pandas as pd
from decimal import Decimal


df = pd.read_csv("data.csv")

for _, row in df.iterrows():
    publish.single("Temperature",
    Decimal(row["Temperature"].item()),
    hostname="host.docker.internal")
    time.sleep(10)
