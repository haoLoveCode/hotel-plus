<template>
  <el-upload
      :accept="accept"
      class="upload-demo"
      :action="uploadApi"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :on-exceed="handleExceed"
      :file-list="fileList"
      :limit="1"
      :data="upLoadParams"
      :on-success="onSuccess"
      :on-error="onError"
      :headers="headers"
      ref="Uploader"
  >
    <el-button type="primary" size="mini">{{ buttonText }}</el-button>
  </el-upload>
</template>
<script>
export default {
  name: "UploadVideo",
  props: {
    buttonText: {
      type: String,
      required: true,
      default: '上传视频',
    },
    headers: {
      token: ''
    },
    accept: {
      type: String,
      required: false,
      default: () => {
        return '.mp4'
      },
    },
    uploadApi: {
      required: true,
    },
    fileList: {
      type: Array,
      required: false,
      default: () => []
    },
    upLoadParams: {
      type: Object,
      required: false,
      default: () => []
    },
  },
  data() {
    return {
      type: 1, //没用的
    };
  },
  methods: {
    clearFiles() {
      console.log('清除图片')
      this.$refs.Uploader.clearFiles()
    },
    handleRemove(file, fileList) {
      console.log('开始文件删除:'+JSON.stringify(file))
      if(!this.$isNull(file)){
        let name = file.name
        this.$emit("handleFileDelete", name);
      }
      return;
    },
    handlePreview(file) {
      console.log(file);
      console.log(this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message.warning("只能上传一个文件");
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    onError(err, file, fileList) {
      this.$message.warning(err.message);
    },
    onSuccess(response, file, fileList) {
      if (!this.$isNull(response)) {
        let data = response.data
        if (!this.$isNull(data)) {
          this.$emit("onSuccess", data);
        }
      }
      return;
    },
  },
};
</script>
