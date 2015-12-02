package com.korwang.shuishui.utils;

public class AddFile {
	/**
	 Author:wx
	 * Version:1.0
	 	1、安装 aptana 插件：
		 * 依次点击：Help -> Install New Software... -> Add
		 * 输入：http://download.aptana.com/studio3/plugin/install
		 * 选择：Aptana Studio 3 Plugin，并进行安装
		 * 重启 eclipse
		
		2、安装 svn 插件：
		 * 依次点击：Help -> Install New Software... -> Add
		 * 输入：http://subclipse.tigris.org/update_1.6.x
		 * 选择：Subclipse (Required)、Subversion Client Adapter (Required)、Subversion JavaHL Native Library Adapter (Required)，并进行安装
		 * 安装libsvn-java（Ubuntu 下需要安装）：sudo apt-get install libsvn-java
		 * 重启 eclipse
		
		3、安装 git 插件：
		 * 依次点击：Help -> Install New Software... -> Add
		 * 输入：http://download.eclipse.org/egit/updates
		 * 选择：Eclipse EGit，并进行安装
		 * 重启 eclipse
		
		4、安装 Java EE 插件：
		 * 依次点击：Help -> Install New Software... -> Add
		 * 输入：http://download.eclipse.org/releases/indigo
		 * 选择：Eclipse Java EE Developer Tools，并进行安装
		 * 重启 eclipse
		
		5、安装 JSHint 插件：
		* 依次点击：Help -> Install New Software... -> Add
		 * 输入：http://github.eclipsesource.com/jshint-eclipse/updates/
		 * 选择：JSHint，并进行安装
		 * 重启 eclipse
		 * 
		tomcat http://www.eclipsetotale.com/tomcatPlugin.html
		download  tomcatplugin
		add resource variable
		CATALINA_BASE  PATH
		CATALINA_HOME  PATH
		TOMCAT_HOME PATH
	 */
	public AddFile(){
		
	}
	/**
 	public abstract class AsyncTask<Params, Progress, Result> {}
 	
	 三种泛型类型分别代表“启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”。
	 
	 在特定场合下，并不是所有类型都被使用，如果没有被使用，可以用java.lang.Void类型代替。
	 
	一个异步任务的执行一般包括以下几个步骤：
	
	1.execute(Params... params)，执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行。
	
	2.onPreExecute()，在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对
		
		UI做一些标记。
		
	3.doInBackground(Params... params)，在onPreExecute()完成后立即执行，用于执行较为费时的
	
		操作，此方法将接收输入参数和返回计算结果。在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
	4.onProgressUpdate(Progress... values)，在调用publishProgress(Progress... values)时，
	
		此方法被执行，直接将进度信息更新到UI组件上。
	5.onPostExecute(Result result)，当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，
	
		直接将结果显示到UI组件上。
		
	在使用的时候，有几点需要格外注意：
	
	1.异步任务的实例必须在UI线程中创建。
	
	2.execute(Params... params)方法必须在UI线程中调用。
	
	3.不要手动调用onPreExecute()，doInBackground(Params... params)，
	
		onProgressUpdate(Progress... values)，onPostExecute(Result result)这几个方法。
		
	4.不能在doInBackground(Params... params)中更改UI组件的信息。
	
	5.一个任务实例只能执行一次，如果执行第二次将会抛出异常。
	 * 
	 */
}
