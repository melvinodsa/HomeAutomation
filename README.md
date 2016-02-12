# HomeAutomation
Simple demo app to control the home appliances with mobile app.
It has a backend server which can be accessed from [here](https://github.com/melvinodsa/HomeAutomationBackend).

Download the apk from the latest [release](https://github.com/melvinodsa/HomeAutomation/releases). Install it in the mobile. Before running the app go and install the [HomeAutomationBackend](https://github.com/melvinodsa/HomeAutomationBackend). The instructions are given in the README of HomeAutomationBackend. Just note the ip address of the server w.r.t the network the mobile device is accessing. Mobile and server should be in same network or mobile should have means to access the server network.

In app starts it asks for the credentials. As of now username :- **admin**, password :- **admin**. Also give the ipaddress of the server. eg:- http://192.168.150.1:9090. Note by default the server runs at 9090 port. You could change it if you wish to.

![Login Page](https://github.com/melvinodsa/HomeAutomation/blob/master/login.png "Login Page")

After successful login you will process to the home page.
![Home Page](https://github.com/melvinodsa/HomeAutomation/blob/master/home1.png "Home Page")

Now you can update the usage details through the demo page (which is explained [here](https://github.com/melvinodsa/HomeAutomationBackend)). The changes will reflect in the app with in 10 seconds. Also user can switch of and on the light and it can be verified in the Demo page.
![Updated home Page](https://github.com/melvinodsa/HomeAutomation/blob/master/home2.png "Updated home Page")
