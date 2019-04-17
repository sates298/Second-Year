from csma_cd import *
import time
import random


class Computer:

    def __init__(self, name, p=0.5, max_sleep=100, wire_length=10):
        self.probability = p
        self.name = name
        self.wire_length = wire_length
        self.message = self.create_new_msg()
        self.max_sleep = max_sleep
        self.sleep = self.random_sleep()
        self.is_sender = False
        self.is_receiver = False

    def create_new_msg(self):
        random.seed(int(round(time.time() * random.randint(500, 1500))))
        return random.randint(2*self.wire_length, 2.5*self.wire_length)

    def random_sleep(self):
        random.seed(int(round(time.time() * random.randint(500, 1500))))
        curr_max_sleep = int((1 - self.probability) * self.max_sleep)
        r = random.randint(1, curr_max_sleep)
        return r

    def stop_sending(self):
        self.is_sender = False
        self.sleep = self.random_sleep()

    def decrease_sleep(self):
        if not self.is_sender and not self.is_receiver:
            self.sleep -= 1
            if self.sleep == 0:
                self.is_sender = True
                return self.message
        return 0

    def print_info(self):
        print('Computer: ' + self.name)
        print('Message Length: ' + str(self.message))
        print('Sleep: ' + str(self.sleep))
