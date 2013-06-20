package net.rauros.groovy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

class ExportConfig extends DefaultTask {
	def String target = 'config'
	def String url = 'http://open-zwave.googlecode.com/svn/trunk/config'
	@TaskAction
	def export() {
		File exportDir = new File(target)
		if(exportDir.exists())
		{
			return;
		}
		exportDir.mkdirs();
		
		SVNURL url = SVNURL.parseURIEncoded(url)
		SVNRepository repository = SVNRepositoryFactory.create(url);
		long latestRevision = repository.getLatestRevision();
		SVNClientManager ourClientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		updateClient.doExport(
			repository.getLocation(), exportDir, SVNRevision.create(latestRevision), SVNRevision.create(latestRevision),
			null, true, SVNDepth.INFINITY);
	}
}