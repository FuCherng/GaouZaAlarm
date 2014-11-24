__author__ = 'GPLlab'

import cherrypy
import pymongo

# cherrypy -> for server
# pymongo -> for database
#27017

class Hello:
    def __init__(self):
        mongo = pymongo.MongoClient("127.0.0.1", 27017)
        self.db = mongo["GaouZa"]
        # In "Users" collection : set unique username/u_id, and index username/u_id
        self.db.Users.ensure_index([('username', 1)], unique=True)
        self.db.Users.ensure_index([('u_id', 1)], unique=True)

        # In "Alarm" collection : set index u_id
        self.db.Alarm.ensure_index([('u_id', 1)])
        # In "Alarms" collection : set unique (u_id + time), (u_id + alive)
        self.db.Alarm.ensure_index([('u_id', 1), ('time', 1)], unique=True)
        self.db.Alarm.ensure_index([('u_id', 1), ('alive', 1)], unique=True)

        # In "Wakers" collection : set index u_id/who
        self.db.Wakers.ensure_index([('u_id', 1)])
        self.db.Wakers.ensure_index([('who', 1)])
        # In "Waker" collection : set unique u_id + who
        self.db.Wakers.ensure_index([('u_id', 1), ('who', 1)], unique=True)

        # In "Friends" collection : set unique u_id
        self.db.Friends.ensure_index([('u_id', 1)], unique=True)

    #================================ Register / Login ================================
    @cherrypy.expose
    def register(self, r_username, r_pwd):
        print r_username, r_pwd
        # track unique username and return log to user
        idnum = str(self.db.Users.count() + 1)
        try:
            self.db.Users.insert({"u_id": "00"+idnum, "username": r_username, "pwd": r_pwd, "picname": "00"+idnum+"_pic"})
        except Exception, error:
            print 'error:', str(error)
            # return string to client
            return "This username " + r_username + " is used, please choose another one :D"
        self.db.Friends.insert({"u_id": "00"+str(self.db.Users.count()), "agree": [], "ask": [], "wait": [], "black": []})
        return "Congratulation ! You register successfully"

    @cherrypy.expose
    def login(self, l_username, l_pwd):
        print l_username, l_pwd
        result = self.db.Users.find_one({"username": l_username, "pwd": l_pwd})
        print result
        # find the account or not
        if result is None:
            return "Login error, please check your username or password again"
        else:
            return "Successfully login !"

    #================================ Function : set/modify alarm ================================
    @cherrypy.expose
    def set_alarm(self, aid, atime, amsg):
        # format : u_id,time(year_month_day_xx:yy),message
        print aid, atime, amsg
        #u_id = "001"
        #time = "2014_07_20_08:00"
        #msg = "Happy birthday to me !"
        try:
            self.db.Alarm.insert({"u_id": aid, "time": atime, "msg": amsg, "alive": True})
        except Exception, error:
            print 'error:', str(error)
            #self.db.Alarm.update({"u_id": aid, "time": atime}, )
            return "You have already a alarm at " + atime + ". Please modify it."
        return "OK ! alarm is set."

    @cherrypy.expose
    def modify_alarm(self, aid, atime, amsg):
        print aid, atime, amsg
        try:
            # check whether a alarm is alive
            now_alarm = self.db.Alarm.find_one({"u_id": aid, "alive": True})
        except Exception, error:
            return "You have not set any alarm..."
        print now_alarm
        self.db.Alarm.update({"u_id": aid, "alive": True}, {"$set": {"time": atime, "msg": amsg}})
        return "modify the alarm to " + atime + " and message: " + amsg + "."

    #================================ Function : Waker : set morning ===============================
    @cherrypy.expose
    def search_alarms(self,user_id):
        # Search user's friends...
        try:
            data = self.db.Friends.find_one({"u_id": user_id})
        except Exception, error:
            return "No friends"
        tmp = str(data).find("'agree'")
        tmp = tmp + 9
        endtmp = tmp
        while str(data)[endtmp] != ']':
            endtmp = endtmp+1
        friendstring = str(data)[tmp+3: endtmp-1]
        friendlist = friendstring.split("', u'")
        #print friendlist[0] + ", " + friendlist[1] + ", " + friendlist[2]
        # for each friend, check whether exiting alarms or not
        level1 = False
        level2 = False
        all = list()
        for i in range(0, len(friendlist), 1):
            print str(friendlist[i])
            try:
                # friend has alarm is alive
                IsAlarm = self.db.Alarm.find_one({"u_id": str(friendlist[i]), "alive": True})
                #print IsAlarm
                if IsAlarm is None:
                    print "Find no alarm"
                    level1 = True
            except Exception, error:
                print str(IsAlarm)
            if level1 == False:
                print "hello"
                try:
                    IsWaker = self.db.Wakers.find_one({"u_id": user_id, "who": friendlist[i]})
                    if IsWaker is None:
                        print "not yet be his(her) waker"
                        level2 = True
                        try:
                            IsFull10 = self.db.Wakers.find({"who": friendlist[i]}).count()
                            print IsFull10
                        except Exception, error:
                            #Cardtmp = str(self.db.Users.find_one({"u_id": user_id}, {"$project": {"u_id": 0, "username": 1, "pwd": 0, "picname": 1}}))# {"u_id":0, "username": 1, "pwd": 0, "picname": 1})# + "_" + str(self.db.Alarm.find({"u_id": user_id, "alive": True}, {"u_id":0, "time": 1, "msg": 1, "alive": 0}))
                            print Cardtmp
                        if IsFull10 < 10:
                            #print "<10"
                            Cardtmp = str(self.db.Users.find_one({"u_id": friendlist[i]}, fields = {"username": True, "picname": True, "_id": False})) + "_" + str(self.db.Alarm.find_one({"u_id": friendlist[i], "alive": True}, fields = {"_id": False, "time": True, "msg": True}))
                            print Cardtmp
                            all.append(Cardtmp)
                except Exception, error: # not yet be his(her) waker
                    print str(IsWaker)
                if level2 == False:
                    print "Already be his(her) waker"
            level1 = False
            level2 = False
        return str(all)

    #================================ Function : Waker : set morning ===============================
    @cherrypy.expose
    def set_morning(self, wid, wwho, wmsg, wsound):
        print wid, wwho, wmsg, wsound
        # the one you want to wake is allow you to do this! (10 friends)
        allwakers = list()
        onewaker = self.db.Wakers.find_one({"who": wwho})
        #print onewaker
        if onewaker is not None:
            for waker in self.db.Wakers.find({"who": wwho}):
                #print waker
                allwakers.append(waker)
            print len(allwakers)
            if len(allwakers) >= 10:
                return "You can not be a waker."
            else:
                try:
                    self.db.Wakers.insert({"u_id": wid, "who": wwho, "msg": wmsg, "sound": wsound})
                except Exception, error:
                    print 'error:', str(error)
                    return "Do not repeat to waker me up."
                return "Successfully be a waker !"
        else:
            #try:
            #    self.db.Wakers.insert({"u_id": wid, "who": wwho, "msg": wmsg, "sound": wsound})
            #except Exception, error:
            #    print 'error:', str(error)
            #    return "Do not repeat to waker me up."
            return "Successfully be a waker !"

    #================================ Function : Adding friends ===============================
    @cherrypy.expose
    def ask_friend(self, f_id, ask_id):
        #check friendship
        print f_id, ask_id
        allf = self.db.Friends.find_one({"u_id": f_id, "agree": {"$elemMatch": {"$in": [ask_id]}}})
        if allf is not None:
            return "You are already friends."
        else:
            self.db.Friends.update({"u_id": f_id}, {"$addToSet": {"ask": ask_id}})
            self.db.Friends.update({"u_id": ask_id}, {"$addToSet": {"wait": f_id}})
            return "Send ask... "

    @cherrypy.expose
    def agree_friend(self, f_id, agree_id):
        # Add to each friendship
        print f_id, agree_id
        self.db.Friends.update({"u_id": f_id}, {"$addToSet": {"agree": agree_id}})
        self.db.Friends.update({"u_id": agree_id}, {"$addToSet": {"agree": f_id}})
        # Delete f_id 's wait : agree_id + agree_id 's f_id
        self.db.Friends.update({"u_id": f_id}, {"$pull": {"wait": agree_id}})
        self.db.Friends.update({"u_id": agree_id}, {"$pull": {"ask": f_id}})
        return "You are friends now!"

    #================================ Query test for debugging ================================
    @cherrypy.expose
    def query_users(self):
        all = list()
        for data in self.db.Users.find():
            print data
            all.append(data)
        return str(all)

    @cherrypy.expose
    def data_test(self, data_test):
        print data_test
        return "OK i got it"
# connection to network

cherrypy.quickstart(Hello(), config="config.ini")
