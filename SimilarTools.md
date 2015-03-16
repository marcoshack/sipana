The [How To Debug and Troubleshoot VOIP](http://www.voip-info.org/wiki/view/How+To+Debug+and+Troubleshoot+VOIP) page of voip-info has a good list of available tools for SIP monitoring and troubleshooting.

<br>

<h1><a href='http://manageengine.adventnet.com/products/vqmanager/'>VQManager</a></h1>

<ul><li>VQManager is a proprietary system;<br>
</li><li>The current version doesn't have distributed clients/sniffers to capture and send information to a server. Each environment should have its own VQManager installation as described in this <a href='http://forums.adventnet.com/viewtopic.php?t=701791'>thread</a>;</li></ul>

<br>

<h1><a href='http://www.wireshark.org/'>Wireshark (Ethereal)</a></h1>

<ul><li>Standalone generic protocol analyzer that has some SIP and RTP features<br>
</li><li>Good SIP call flow feature for troubleshooting</li></ul>

<br>

<h1><a href='http://www.sipient.com/distributed.html'>Distributed SIPFlow</a></h1>

<ul><li>It only creates SIP flows, there are no historical nor statistic information;<br>
</li><li>The console is a desktop graphic interface (Java), no remote visualization;<br>
</li><li><i>Check if the console is able to persist the data</i>;</li></ul>

<br>

<h1><a href='http://ant.comm.ccu.edu.tw/sip/'>SIPAnalyzer</a></h1>

<ul><li>No development since May 2004;<br>
</li><li>Only capture and send the raw SIP data to the server and provides a web interface to list and view the packets;