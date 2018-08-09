package com.altsoft.Framework;

import android.util.Log;

import com.groupbyinc.common.apache.commons.net.ftp.FTP;
import com.groupbyinc.common.apache.commons.net.ftp.FTPClient;
import com.groupbyinc.common.apache.commons.net.ftp.FTPFile;
import com.groupbyinc.common.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public  class FtpInfo {
    FTPClient  ftp;
    private final String TAG = "Connect FTP";
    public FtpInfo(String host, String user, String pwd,int port  ) throws Exception
    {
        ftp = new FTPClient();
        ftpConnect(host,user,pwd,port);
    }

    public boolean ftpConnect(String host, String username, String pwd, int port) {
        boolean result = false;
        try{
            ftp.connect(host, port);

            if(FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                result = ftp.login(username, pwd);
                ftp.enterLocalPassiveMode();
            }
        }catch (Exception e){
            Log.d(TAG, "Couldn't connect to host");
        }
        return result;
    }

    public boolean ftpDisconnect() {
        boolean result = false;
        try {
            ftp.logout();
            ftp.disconnect();
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "Failed to disconnect with server");
        }
        return result;
    }
    ///현재 작업 경로 가져오기
    public String ftpGetDirectory(){
        String directory = null;
        try{
            directory = ftp.printWorkingDirectory();
        } catch (Exception e){
            Log.d(TAG, "Couldn't get current directory");
        }
        return directory;
    }
    /// 작업 경로 수정
    public boolean ftpChangeDirctory(String directory) {
        try{
            ftp.changeWorkingDirectory(directory);
            ftp.changeWorkingDirectory(directory);
            return true;
        }catch (Exception e){
            Log.d(TAG, "Couldn't change the directory");
        }
        return false;
    }
    ///directory 내 파일 리스트 가져오기
    public String[] ftpGetFileList(String directory) {
        String[] fileList = null;
        int i = 0;
        try {
            FTPFile[] ftpFiles = ftp.listFiles(directory);
            fileList = new String[ftpFiles.length];
            for(FTPFile file : ftpFiles) {
                String fileName = file.getName();

                if (file.isFile()) {
                    fileList[i] = "(File) " + fileName;
                } else {
                    fileList[i] = "(Directory) " + fileName;
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }


   /// 새로운 directory 생성 및 삭제
    public boolean ftpCreateDirectory(String directory) {
        boolean result = false;
        try {
            result =  ftp.makeDirectory(directory);
        } catch (Exception e){
            Log.d(TAG, "Couldn't make the directory");
        }
        return result;
    }

    public boolean ftpDeleteDirectory(String directory) {
        boolean result = false;
        try {
            result = ftp.removeDirectory(directory);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove directory");
        }
        return result;
    }






    ///파일 삭제
    public boolean ftpDeleteFile(String file) {
        boolean result = false;
        try{
            result = ftp.deleteFile(file);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove the file");
        }
        return result;
    }






    ///파일 이름 변경
    public boolean ftpRenameFile(String from, String to) {
        boolean result = false;
        try {
            result = ftp.rename(from, to);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't rename file");
        }
        return result;
    }






    //// 파일 다운로드
    public boolean ftpDownloadFile(String srcFilePath, String desFilePath) {
        boolean result = false;
        try{
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            FileOutputStream fos = new FileOutputStream(desFilePath);
            result = ftp.retrieveFile(srcFilePath, fos);
            fos.close();
        } catch (Exception e){
            Log.d(TAG, "Download failed");
        }
        return result;
    }

    /// FTP 서버에 파일 업로드
    public boolean ftpUploadFile(String srcFilePath, String desFileName, String desDirectory) {
        boolean result = false;
        try {
            FileInputStream fis = new FileInputStream(srcFilePath);
            if(ftpChangeDirctory(desDirectory)) {
                result = ftp.storeFile(desFileName, fis);
            }
            fis.close();
        } catch(Exception e){
            Log.d(TAG, "Couldn't upload the file");
        }
        return result;
    }
}
