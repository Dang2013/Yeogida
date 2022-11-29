from django.db import models


class Tourlist(models.Model):
    name = models.CharField(max_length=300)
    address = models.CharField(max_length=500)
    category = models.CharField(max_length=100)
    regional_local = models.CharField(max_length=500)
    basic_local = models.CharField(max_length=500)
    with_keyword = models.CharField(max_length=1000)
    trip_keyword = models.CharField(max_length=1000)
    keywords = models.CharField(max_length=2000)
