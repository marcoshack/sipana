

<br>

<h1>Requisites</h1>

<ul><li>Maven2 (<a href='http://maven.apache.org/'>http://maven.apache.org/</a>)<br>
</li><li>Sun JDK 1.6 or higher<br>
</li><li>MySQL server 5.x</li></ul>

<br>

<h1>Checkout</h1>

Follow instructions in <a href='http://code.google.com/p/sipana/source/checkout'>checkout</a> page. Hereby we'll refer to $WORKDIR as the parent directory where you did the project checkout.<br>
<br>
<br>

<h1>Build</h1>

<pre><code> cd $WORKDIR/sipana<br>
 mvn<br>
</code></pre>

This process take a while because maven will download all required packages and maven plugins.<br>
<br>
<br>

<h1>IDE configuration</h1>

<h2>Netbeans 6.5</h2>

<h3>Install and Configure Maven plugin</h3>

<ul><li>Install Netbeans Maven plugin: ''Tools / Plugins / Search: Maven / Install''</li></ul>

<ul><li>Configure Maven options: ''Toos / Options / Miscellaneous / Maven''</li></ul>

<img src='http://sipana.googlecode.com/svn/images/nb65-maven_plugin_options.png' />

<h4>Open Sipana project</h4>

<ol><li>Open menu ''File / Open Project''<br>
</li><li>Find the project location<br>
</li><li>Check the option ''"Open Required Projects"'' to open all projects in your workspace<br>
</li><li>Click ''Open Project'' button</li></ol>

<img src='http://sipana.googlecode.com/svn/images/nb65-open_project.png' />

<br>

<h1>Database creation</h1>

In order to run the Sipana server component you need to create and setup the MySQL database.<br>
<br>
<br>
# Create database (assuming you're using user ''root'' with password):<br>
<br>
<pre><code>mysql -u root -p &lt; $WORKDIR/sipana/sql/sipana_db_create.sql<br>
</code></pre>

# Create and give permissions to ''sipana'' MySQL user:<br>
<br>
<pre><code>mysql -u root -p sipana<br>
grant all privileges on sipana.* to 'sipana'@'localhost' identified by 'sipana';<br>
</code></pre>

<br>

<h1>See also</h1>

<ul><li><a href='SipanaModules.md'>Sipana Modules</a>
</li><li><a href='SipanaArchitecture.md'>Sipana Architecture</a>
</li><li><a href='Roadmap.md'>Project Roadmap</a>
</li><li><a href='http://code.google.com/p/sipana/issues/list'>Issue list</a>