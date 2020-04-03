import io
import googlemaps
from datetime import datetime
import csv
gmaps = googlemaps.Client(key='AIzaSyA6HfyNIoKocEW64pVsZcxvseySI1zpwR4')
address=[]
with open('df.csv', 'r') as file:
    reader = csv.reader(file)
    for row in reader:
        lat=row[-2]
        lon=row[-1]
        if lat =='right':
            continue
        reverse_geocode_result = gmaps.reverse_geocode((lon, lat))
        print(lat,lon)
        with open('address.csv','a', encoding='utf-8') as f:
            writer = csv.writer(f)
            row.append(reverse_geocode_result[-0]['formatted_address'])
            writer.writerow(row)
       
