package service;


public interface CrawlerManager {

	public String findRecord(String sId, String tId);

	public String createRecord(String sId, String tId, String code, String cookie);

	public String findCheckCode(String path);

	public String updateTable(String timeStamp);

}
