

<br><br>

<h1>Sipana Server</h1>

<h2>JBoss Application Server</h2>

The Sipana server component is a Java EE application developed to run in the <a href='http://www.jboss.org/jbossas/'>JBoss Application Server</a>. Before install and run the Sipana Server you need to install the Java Virtual Machine (JRE or JDK) version 5 or higher and the JBoss AS version 4.2.3.GA or higher.<br>
<br>
<br>
The Sipana Server will be installed in the <b>default</b> JBoss server deployment ($JBOSS_HOME/server/default). We recommend you to configure the environment variable <b>$JBOSS_HOME</b> with the patch for you JBoss AS installation because we'll use it in all command line commands in this document so you wont need to replace it with your server installation path:<br>
<br>
<pre><code> export JBOSS_HOME=/usr/local/jboss-4.2.3.GA<br>
</code></pre>

<br>

<h3>MySQL server and MySQL JDBC driver</h3>

By default Sipana server uses the MySQL as background DBMS, so you will need a up and running MySQL server.<br>
<br>
The MySQL JDBC driver, needed to connect to a MySQL server from a JBoss AS application, is not installed in the JBoss AS by default. Please download the correct version for you MySQL server <a href='http://dev.mysql.com/downloads/connector/j/'>here</a> and copy the file <b>mysql-connector-java-X.X.X-bin.jar</b> to the <b>$JBOSS_HOME/server/$JBOSS_SERVER/lib</b> directory.<br>
<br>
<br>

<h2>Sipana Server component</h2>

<h3>Download and Install</h3>

<a href='http://code.google.com/p/sipana/downloads/list'>Download</a> the last version of the Sipana Server component and unpack it in a local directory<br>
<br>
<pre><code> tar zxvf sipana-server-X.X.X.tar.gz<br>
 cd sipana-server-X.X.X<br>
</code></pre>


Copy '''sipana.ear''' to the JBoss AS server deploy directory:<br>
<br>
<pre><code> cp -R sipana.ear $JBOSS_HOME/server/$JBOSS_SERVER/deploy<br>
</code></pre>

<br>

<h3>Create the database</h3>

You will need <b>root</b> access or equivalent to execute the database creation script:<br>
<br>
<pre><code> mysql -u root -p &lt; sipana-server/sql/sipana_db_create.sql<br>
</code></pre>

Configure a username and password with all privileges to be used by Sipana Server:<br>
<br>
<pre><code> mysql -u root -p<br>
</code></pre>

<pre><code> grant all privileges on sipana.* to 'sipana'@'localhost' identified by 'sipana';<br>
 flush privileges;<br>
</code></pre>

Please look in the <a href='http://dev.mysql.com/doc/'>MySQL documentation</a> for more information about creating and configure the database.<br>
<br>
<br>

<h3>Configure the database parameters</h3>

Edit the file <b>sipana-server-ds.xml</b> in the directory <b>$JBOSS_HOME/server/default/deploy</b> and change the following parameters as needed.<br>
<br>
<pre><code> vim $JBOSS_HOME/server/default/deploy/sipana-server-ds.xml<br>
</code></pre>

The <b>connection-url</b> attribute format is: <i>jdbc:mysql://HOST:PORT/DATABASE_NAME</i>

<pre><code> &lt;connection-url&gt;jdbc:mysql://localhost:3306/sipana&lt;/connection-url&gt;<br>
 &lt;user-name&gt;sipana&lt;/user-name&gt;<br>
 &lt;password&gt;sipana&lt;/password&gt;<br>
</code></pre>


<h2>Run the JBoss AS</h2>

<pre><code> $JBOSS_HOME/bin/run.sh -c default -b SERVER_ADDRESS<br>
</code></pre>

<b>IMPORTANT</b>: You cannot use <b>0.0.0.0</b> as -b parameter value otherwise the client component wont be able to connect to the server (see <a href='http://code.google.com/p/sipana/issues/detail?id=4'>Issue #4</a>)<br>
<br>
Please look in the <a href='http://www.jboss.org/jbossas/docs/index.html'>JBoss AS documentation</a> for more information about server operation.<br>
<br>
<br>

<h2>Test the web interface</h2>

Open a web browser and point to the following address to test your installation. Change the string SERVER_ADDRESS to the server IP address:<br>
<br>
<pre><code>  http://SERVER_ADDRESS/sipana/&lt;/nowiki&gt;<br>
</code></pre>


<br>
<br>
<br>

<h1>Sipana Agent</h1>

<h2>Jpcap library</h2>

The Sipana Client component uses <i>Jpcap</i> library (a JNI layer to use native libpcap/WinPcap library). We use the <a href='http://netresearch.ics.uci.edu/kfujii/jpcap/doc/install.html'>Keita Fujii jpcap implementation</a> (there is a Source jpcap project but it seems to be no longer maintained).<br>
<br>
Please take a look in the <a href='http://netresearch.ics.uci.edu/kfujii/jpcap/doc/install.html'>project installation documentation</a> in and read our installation notes below if you have problems during the installation. Fell free to ask for help about jpcap installation in the <a href='http://groups.google.com/group/sipana-dev/'>Sipana mailing list</a>.<br>
<br>
<h3>Jpcap installation notes</h3>

I could install the RPM/DEB packages with no problems in the following Linux distributions:<br>
<br>
<ul><li>Ubuntu 8.10 with Sun JDK<br>
</li><li>CentOS 5.2 with Sun JDK and Sun JRE<br>
<blockquote>use "<i>rpm -ivh --nodeps jpcap-x.x.rpm</i>" to install the RPM if you installed only the Sun JRE rpm package. jpcap RPM package depends on package "jdk" provided only by Sun SDK package)</blockquote></li></ul>

<br>

<h2>Sipana Client</h2>

<ul><li><a href='http://code.google.com/p/sipana/downloads/list'>Download</a> the current version of Sipana Client and unpack it in the host where you are going to install the Client component.</li></ul>

<ul><li>Edit the file <b>conf/jndi.properties</b> and set the property <b>java.naming.provider.url</b> with the server address. Change only the IP address/hostname and maintain the prefix <b>jnp://</b> and the port <b>:1099</b>.</li></ul>


<ul><li>Edit the file <b>conf/sipana-client.properties</b> and edit the following properties<br>
<ul><li><b>org.sipana.client.capture.interface</b>: set with the network interface name where you want to capture or <b>any</b> to capture in all interfaces simultaneously.<br>
</li><li><b>org.sipana.client.capture.filter</b>: set the <i>pcap</i> capture filter (<i>p.ex. "udp and port 5060"</i>). Please refer to <a href='http://www.tcpdump.org/#documentation'>tcpdump/libpcap documentation</a> for more information.</li></ul></li></ul>


<ul><li>Start the client (with <b>root</b> privileges)</li></ul>

<pre><code> $SIPANA_HOME/bin/sipana-client.sh<br>
</code></pre>

<blockquote>where $SIPANA_HOME is the Sipana Client installation directory.</blockquote>

<br>