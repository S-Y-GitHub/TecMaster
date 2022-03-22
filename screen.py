import atexit
import sys
import threading
import serial
import device

args=sys.argv

SERIAL_NUMBER=args[1]

comport=serial.Serial(device.getDevice(SERIAL_NUMBER))

atexit.register(comport.close)

def send():
    while True:
            comport.write(input().encode("UTF-8"))
            comport.flush()

def receive():
    while True:
        if(comport.in_waiting>0):
            print(comport.read().decode("UTF-8"),end="",flush=True)
            
threading.Thread(target=send).start()
threading.Thread(target=receive).start()