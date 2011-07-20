package edu.mit.media.hd.funf.configured;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.json.JSONException;

import android.os.Bundle;

public class FunfConfigTests extends TestCase {

	
	public void testConfigToJson() throws JSONException {
		Map<String, Bundle[]> dataRequests = new HashMap<String, Bundle[]>();
		Bundle bundle = new Bundle();
		bundle.putBoolean("param1", true);
		bundle.putString("param2", "test");
		dataRequests.put("test.probe", new Bundle[]{ bundle });
		Map<String, ProbeDatabaseConfig> databases = new HashMap<String,ProbeDatabaseConfig>();
		databases.put("test_db", new ProbeDatabaseConfig(new String[]{"test.probe"}, "http://test.upload.url.com", null));
		FunfConfig config = new FunfConfig(1, 
				"http://funf.media.mit.edu", 
				hoursToSeconds(3), 
				hoursToSeconds(5),
				0L,
				databases, 
				dataRequests);
		System.out.println(config.toJson());
		FunfConfig parsedConfig = new FunfConfig(config.toJson());
		assertEquals(1, parsedConfig.getVersion());
		assertEquals("http://funf.media.mit.edu", parsedConfig.getConfigUrl());
		assertEquals(hoursToSeconds(3), parsedConfig.getConfigUpdatePeriod());
		assertEquals(hoursToSeconds(5), parsedConfig.getArchivePeriod());
		assertEquals(FunfConfig.DEFAULT_REMOTE_ARCHIVE_PERIOD, parsedConfig.getRemoteArchivePeriod());
		assertTrue(parsedConfig.getDatabases().containsKey("test_db"));
		assertEquals("http://test.upload.url.com", parsedConfig.getDatabases().get("test_db").getUploadUrl());
		assertEquals("test.probe", parsedConfig.getDatabases().get("test_db").getProbesToRecord()[0]);
		assertNull(parsedConfig.getDatabases().get("test_db").getEncryptionKey());
	}
	
	private static long hoursToSeconds(int hours) {
		return 60 * 60 * hours;
	}
}
