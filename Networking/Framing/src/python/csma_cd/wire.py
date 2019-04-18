from csma_cd.computer import *


class Wire:

    def __init__(self, l=10):
        self.length = l
        self.computer1 = Computer('1', wire_length=l)
        self.computer2 = Computer('2', wire_length=l)
        self.channel = [EMPTY_FIELD for _ in range(l)]
        self.msg1 = 0
        self.msg2 = 0

    def print_turn(self):
        tmp = 'computer: ' + self.computer1.name + '['
        for ch in self.channel:
            tmp += ' ' + ch
        tmp += ' ]computer: ' + self.computer2.name
        print(tmp)
        self.computer1.print_info()
        self.computer2.print_info()
        print('left bits from computer:  ', self.computer1.name, str(self.msg1))
        print('left bits from computer:  ', self.computer2.name, str(self.msg2))

    def update_channel(self):
        collision = -1

        if self.channel[0] == self.computer2.name:
            self.computer1.is_receiver = True
            self.channel[0] = EMPTY_FIELD
        else:
            self.computer1.is_receiver = False
        if self.channel[self.length - 1] == self.computer1.name:
            self.computer2.is_receiver = True
            self.channel[self.length - 1] = EMPTY_FIELD
        else:
            self.computer2.is_receiver = False

        for i in range(1, self.length):
            curr2right = self.channel[i]
            left2right = self.channel[i - 1]

            curr2left = self.channel[self.length - 1 - i]
            right2left = self.channel[self.length - i]

            if curr2left == self.computer1.name and right2left == EMPTY_FIELD:
                self.channel[self.length - i] = curr2left
                self.channel[self.length - 1 - i] = EMPTY_FIELD
            elif curr2left == COLLISION_FIELD and collision < 0:
                self.channel[self.length - i] = COLLISION_FIELD
            elif curr2left == self.computer1.name and right2left == self.computer2.name:
                collision = self.length - 1 - i

            if left2right == EMPTY_FIELD and curr2right == self.computer2.name:
                self.channel[i - 1] = curr2right
                self.channel[i] = EMPTY_FIELD
            elif curr2right == COLLISION_FIELD and collision < 0:
                self.channel[i - 1] = COLLISION_FIELD
            elif left2right == self.computer1.name and curr2right == self.computer2.name:
                collision = i

        if collision >= 0:
            self.channel[collision] = COLLISION_FIELD
        if self.msg1 > 0:
            if self.channel[0] == EMPTY_FIELD and self.computer1.is_sender:
                self.channel[0] = self.computer1.name
                self.msg1 -= 1
            elif self.channel[0] == COLLISION_FIELD and self.computer1.is_sender:
                self.computer1.stop_sending()
            if self.msg1 == 0:
                self.computer1.message = self.computer1.create_new_msg()
                self.computer1.stop_sending()
        if self.msg2 > 0:
            if self.channel[self.length - 1] == EMPTY_FIELD and self.computer2.is_sender:
                self.channel[self.length - 1] = self.computer2.name
                self.msg2 -= 1
            elif self.channel[self.length - 1] == COLLISION_FIELD and self.computer2.is_sender:
                self.computer2.stop_sending()
            if self.msg2 == 0:
                self.computer2.message = self.computer2.create_new_msg()
                self.computer2.stop_sending()
        if self.channel[0] == self.channel[self.length - 1] == COLLISION_FIELD:
            self.msg1 = self.computer1.message
            self.msg2 = self.computer2.message
            self.channel = [EMPTY_FIELD for _ in range(self.length)]

    def run(self):
        while True:
            time.sleep(0.8)
            self.update_channel()
            tmp = self.computer1.decrease_sleep()
            if tmp > 0:
                self.msg1 = tmp
            tmp = self.computer2.decrease_sleep()
            if tmp > 0:
                self.msg2 = tmp

            clear()
            self.print_turn()
