# MSmasrtUpload

## 操作流程

### 获取上传实例
  `	MSmartUpload su = MSmartUpload.getInstance();`
  
### 初始化
  `su.initialize(pageContext)` 
  
### 上传表单数据
  `	su.upload();`
### 另存文件
  `su.saveAs(file, destFilePathName);`
  
### 获取文件和非文件数据
 `MFile[] files = su.getArrayFile();`
 


