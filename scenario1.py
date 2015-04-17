import os

filename = "Topology(1).txt"

for i in range(1,20):
	val = chr(ord('A')+i)
	os.system("java -jar node.jar "+val+" \""+filename+"\" &")
