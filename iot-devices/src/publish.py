import paho.mqtt.publish as publish
from random import random
from random import seed
import time

publish.single(f"ClassTest/status",
               "up",
               hostname="host.docker.internal")

seed(1)
for _ in range(10):
    publish.single("ClassTest/temperature",
                   random(),
                   hostname="host.docker.internal")
    time.sleep(1)

publish.single(f"ClassTest/status",
               "down",
               hostname="host.docker.internal")
