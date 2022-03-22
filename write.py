import sys
from time import sleep
import serial
import serial.tools.list_ports
import serialio
import device

args=sys.argv

SERIAL_NUMBER=args[1]
DATA=list()
for i in range(2,len(args)):
    DATA.append(args[i])

dev=device.getDevice(SERIAL_NUMBER)

b=b"\033TWRITE\r\n"

for d in DATA:
    b+=bytes.fromhex(d)

serialio.write(dev,b)
