package com.dongruan.graduation.networkdiskfileservice.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.dongruan.graduation.networkdiskcommon.pojo.FastDFSFile;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;


public class FastDFSClient {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);
	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageClient storageClient;
	private static StorageServer storageServer;

	static {
		try {
			Properties properties = new Properties();
			InputStream input = FastDFSClient.class.getResourceAsStream("/fdfs_client.conf");
			properties.load(input);
//			String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
//			ClientGlobal.init(filePath);
			ClientGlobal.initByProperties(properties);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient(trackerServer, storageServer);
		} catch (Exception e) {
			logger.error("FastDFS Client Init Fail!",e);
		}
	}

	public static String[] upload(FastDFSFile file) {
		logger.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);


		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient(trackerServer, storageServer);
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), null);
		} catch (IOException e) {
			logger.error("IO Exception when uploadind the file:" + file.getName(), e);
		} catch (Exception e) {
			logger.error("Non IO Exception when uploadind the file:" + file.getName(), e);
		}
		logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

		if (uploadResults == null) {
			logger.error("upload file fail, error code:" + storageClient.getErrorCode());
		}
		String [] results = new String[3];
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];
		results[0] = uploadResults[0];
		results[1] = uploadResults[1];

		logger.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
		return results;
	}

	public static InputStream downFile(String groupName, String remoteFileName) {
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient(trackerServer, storageServer);
			byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}
}