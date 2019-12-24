<template>
    <uploader :options="options" :auto-start="true" @complete="$parent.updateFilesName" class="uploader p-3">
        <uploader-unsupport/>
        <uploader-drop>
            <span class="mb-1">拖动文件至此处上传或 </span>
            <uploader-btn class="mb-0 mr-1">选择文件</uploader-btn>
            <uploader-btn class="mb-0" :directory="true">选择文件夹</uploader-btn>
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
                    target: this.host + '/upload',
                    withCredentials: true,
                    testChunks: false,
                    categaryMap: { //用于限制上传的类型
                        image: ["gif", "jpg", "jpeg", "png", "bmp"]
                    }
                }
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
</style>