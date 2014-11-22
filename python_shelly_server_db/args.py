__author__ = 'GPLlab'

def a(a, b, *args):
    print a
    print b
    print args
    return "args", 123

def k(a, b, **kwargs):
    print a
    print b
    print kwargs

print a(1,2,3,4,5)
k(1,2,c=3,d=4,e=5)