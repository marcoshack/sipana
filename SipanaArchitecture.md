## Summary Project architecture overview ##

The diagram bellow illustrates the Architecture decisions in the current version of Sipana. For information about future developments visit the [Issues](http://code.google.com/p/sipana/wiki/Issues?tm=3) page and take a look in the [Marcos Hack's graduation term](http://compmack.googlecode.com/svn/marcoshack/tgi/TGI_Marcos_Hack.pdf) (only in Portuguese).

<img src='http://sipana.googlecode.com/svn/images/sipana_architecture.png' />

## Deployment overview ##
The image bellow ilustrates a possible distributed deployment environment:

<img src='http://sipana.googlecode.com/svn/images/sipana-ditributed.png' />
In each site (Tokyo, São Paulo, and Los Angeles) there are a Sipana Agent running as a network sniffer monitoring all of the Sip Signaling in the LAN.

Only in Tokyo there is a Sipana Server, so all of the agents send the Sip Statistics to the Sipana Server in Tokyo, and anybody could acces the Sipana Server in Tokyo to analyse the performance, and troubleshooting of the whole network.