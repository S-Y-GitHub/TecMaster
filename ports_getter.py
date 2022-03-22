import sys
import serial.tools.list_ports

args=sys.argv

tecs=list()

ports = list(serial.tools.list_ports.comports())

VID=1027
PID=24577

for p in ports:
    if(p.vid==VID)&(p.pid==PID):
        tecs.append(p.serial_number)

print(len(tecs))
for tec in tecs:
    print(tec)