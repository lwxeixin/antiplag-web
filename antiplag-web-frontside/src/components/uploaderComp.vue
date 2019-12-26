<template>
    <uploader :key="status" :options="options" :auto-start="true" @complete="complete" @uploadStart="$parent.uploading = true" class="uploader p-3">
        <uploader-unsupport/>
        <uploader-drop>
            <span class="mb-1">拖动文件至此或点击按钮上传 </span>
            <uploader-btn class="mb-0 mr-1">文件</uploader-btn>
            <uploader-btn class="mb-0 mr-1" :directory="true">文件夹</uploader-btn>
            <button class="uploader-btn border-danger text-danger no-box-shadow" @click="status = !status">清除记录</button>
        </uploader-drop>
        <uploader-list class="bg-light"/>
    </uploader>
</template>

<script>
    export default {
        name: "uploaderComp",
        data () {
            return {
                options: {
                    target: this.host + '/fileManager/uploadFile',
                    withCredentials: true,
                    testChunks: false
                },
                status: true
            }
        },
        methods: {
            complete() {
                this.$parent.uploading = false;
                this.$parent.updateFilesName();
            }
        }
    }
</script>

<style lang="less" scoped>
    .uploader {
        font-size: small;
        .uploader-list {
            height: 400px;
            max-height: 400px;
            overflow: auto;
            overflow-x: hidden;
        }
    }
    .no-box-shadow:focus {
        box-shadow: none!important;
    }
</style>