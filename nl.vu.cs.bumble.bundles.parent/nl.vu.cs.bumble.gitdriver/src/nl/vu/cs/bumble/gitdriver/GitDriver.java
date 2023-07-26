package nl.vu.cs.bumble.gitdriver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


public class GitDriver {
	private final static String branch = "master";
	private final static String modelPath = "/Users/ice/Desktop/Eclipse Plugin/emfcloud-modelserver-collaboration-plugin/examples/org.eclipse.emfcloud.modelserver.example/src/main/resources/workspace/";
	
	// localPath = modelPath + modelName, uri, branch = "master", tempPath
	
	// Fetch: 1. fetch remote model to tempPath; 2. copy model to localPath
	public static boolean fetch(String modelName) {
		boolean isSuccess = false;
		File localFile = new File(modelPath + modelName); 
		String tempPath = System.getProperty("user.dir") + "/temp";
		
		try (Git git = Git.open(new File(tempPath))) {
			PullCommand pull = git.pull();
			pull.setRemoteBranchName(branch); 
			pull.setStrategy(MergeStrategy.RECURSIVE);
			pull.setRebase(true);
			pull.setProgressMonitor(new TextProgressMonitor()); 
			PullResult result = pull.call();
			
			FileUtils.forceDelete(localFile);
			FileUtils.copyFile(new File(tempPath + "/" + modelName), localFile);
			
			isSuccess = result.isSuccessful();
			
		} 
		catch (Exception e) {
			isSuccess = false; 
		}
		
		return isSuccess;
		
	}
	

	// Save: 1. copy model from localpath to tempPath; 2. if .git -> push else -> init & push
	public static boolean save(String modelName, String uri) throws Exception{
		boolean isSuccess = false;

		File localFile = new File(modelPath + modelName); 
		String tempPath = System.getProperty("user.dir") + "/temp";
		File f = new File(tempPath);
		
		if(!f.exists()) {
			CloneCommand cloneCommand = new CloneCommand();
			cloneCommand.setURI(uri);
			cloneCommand.setProgressMonitor(new TextProgressMonitor());
			cloneCommand.setDirectory(new File(System.getProperty("user.dir") +  "/temp"));
			cloneCommand.setBranch("master");
			cloneCommand.call();
			
//			File aa = new File(System.getProperty("user.dir"));
//			File[] list = aa.listFiles();
//			
//			for(File file : list) {
//				if (file.isDirectory() && (new File(file.getAbsolutePath() + "/.git").exists())) {
//					file.renameTo(f);
//					break;
//				}
//			}	
		}
		
		FileUtils.copyFile(localFile, new File(tempPath + "/" + modelName));
		
		Git git = Git.open(new File(tempPath));
		git.add().addFilepattern(".").setUpdate(false).call();
		Status status = git.status().call();
	    
		if (status.hasUncommittedChanges()) {
			git.commit().setMessage("Save model").call();
	    }
		
		PushCommand pushCommand = git.push();
	    pushCommand.setProgressMonitor(new TextProgressMonitor());
	    pushCommand.setPushAll();
	    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider( "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", "" ) );
	    pushCommand.call();
	    isSuccess = true;
	    
		return isSuccess;
	}

}
