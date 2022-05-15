package com.example.studyspinner;

//public class Constant {
//}
//
//package com.mgroup.irondomeapss.Objects;

public class Constants {

    public static class Languages {
        public static String HEBREW_LANGUAGE = "iw";
        public static String ENGLISH_LANGUAGE = "en";

    }
    public static class RangeScore {
        public static int DISABLED = -1;
        public static int MAINTENANCE = -2;
        public static int LOW_RANGE_NO_INFO = 0;
        public static int HIGH_RANGE_NO_INFO = 9;
        public static int LOW_RANGE_HIGH_SCORE = 10;
        public static int HIGH_RANGE_HIGH_SCORE = 39;
        public static int LOW_RANGE_MEDIUM_SCORE = 40;
        public static int HIGH_RANGE_MEDIUM_SCORE = 69;
        public static int LOW_RANGE_LOW_SCORE = 70;
        public static int HIGH_RANGE_LOW_SCORE = 100;
    }

    public static class RangeScoreLock {
        public static int LOW_RANGE_HIGH_SCORE = 0;
        public static int HIGH_RANGE_HIGH_SCORE = 14;
        public static int LOW_RANGE_MEDIUM_SCORE = 15;
        public static int HIGH_RANGE_MEDIUM_SCORE = 40;
    }

    public static class SpinnerChoices{
        public static class Period{
            public static int LAST_WEEK = 0;
            public static int LAST_MONTH = 1;
        }
        public static class TypeEvent{
            public static int ALL = 0;
            public static int REPORTING = 1;
            public static int FAULT = 2;
            public static int PREDICTION = 3;
            public static int TECH = 4;
        }

    }

    public static class Pitch {
        public static int MIN_VALUE_SEEK_BAR = -10;
        public static int MAX_VALUE_SEEK_BAR = 10;
        public static int MIN_VALUE_GOOD = -2;
        public static int MAX_VALUE_GOOD = 2;
    }

    public static class Roll {
        public static int MIN_VALUE_SEEK_BAR = -190;
        public static int MAX_VALUE_SEEK_BAR = -170;
        public static int MIN_VALUE_GOOD = -184;
        public static int MAX_VALUE_GOOD = -176;
    }

    public static class Angle {
        public static int MIN_VALUE_GOOD = 68;
        public static int MAX_VALUE_GOOD = 72;
    }

    public static class Timestamp {
        public static int LIMIT_MINUTES_DO_NOT_GET_DATA = 10;
        public static int COUNTER_SENSORS_DIFFERENCE = 5;
    }

    public static class Server {
        public static String url = "http://34.241.178.69:4056/IronDome/App/";
        public static class General {
            public static class GetRequest {
                public static String getMaintenanceDataN = "http://34.241.178.69:4056/getMaintenanceDataN";
                public static String getSensorDataByTime = "http://34.241.178.69:4056/getSensorDataByTime?";
            }
        }
        public static class Users {
            public static String urlFolder = "Users/";

            public static class GetRequest {
                public static String getPhoneByUsername = url + urlFolder + "getPhoneByUsername?";
                public static String checkUserPassword = url + urlFolder + "checkUserPassword?";
                public static String getAllUserNamesList = url + urlFolder + "usernameList/get";

            }

            public static class PostRequest {
                public static String updateUserViewedEvent = url + urlFolder +  "UserViewedEvent/update";
            }
        }

        public static class Machines {
            public static String urlFolder = "Machines/";
            public static class GetRequest{
                public static String getMachinesIronDome = url + urlFolder + "get/All";
                public static String getMachineIronDome = url + urlFolder + "get?";

            }

            public static class PostRequest {
                public static String updateScoreMachine = url + urlFolder + "score/update";
                public static String updateMaxScoreMachine = url + urlFolder + "maxScore/update";
            }
        }

        public static class Events {
            public static String urlFolder = "Events/";
            public static class GetRequest{
                public static String getEventsIronDome = url + urlFolder + "get/machine/All?";
                public static String getEventsSourceIronDome = url + urlFolder + "get/machine/source/All?";

            }

            public static class PostRequest {
                public static String closeEventIronDome = url + urlFolder + "close";
                public static String updateTechnicianNameToHandleEvent = url + urlFolder + "TechName/update";
            }
        }

        public static class EventsUpDown {
            public static String urlFolder = "EventsUpDown/";
            public static class GetRequest{
                public static String getLastEventUpDownIronDome = url + urlFolder +  "get/last?";
            }

            public static class PostRequest {
            }
        }
    }

    public static class FormatDateTime {
        public static String fullDate = "dd/MM/yyyy - HH:mm:ss";
        public static String Date = "dd.MM.yy";
    }
}
