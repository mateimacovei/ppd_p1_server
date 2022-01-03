import logging
import random
import time

import requests
from promise import *


def myPrint(response, places, showId):
    print(showId)
    print(places)
    print(response.json())


if __name__ == '__main__':
#     for i in range(10):
    while True:
        showId = random.randint(1,3)
        places = [random.randint(1, 100) for _ in range(5)]
        Promise.resolve(
            requests.post(
                'http://localhost:8080/reservation',
                json={
                    "showId": showId,
                    "places": places
                }
            )
        ).then(
            lambda response: myPrint(response, places, showId)
        ).catch(
            lambda error: print(error)
        )
        time.sleep(2)
