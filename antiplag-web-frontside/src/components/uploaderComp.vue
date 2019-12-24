<template>
    <uploader :options="options" :auto-start="true" @complete="$parent.updateFilesName" class="uploader p-3">
        <uploader-unsupport/>
        <uploader-drop>
            <p>Drop files here to upload or </p>
            <uploader-btn class="mr-1">select files</uploader-btn>
            <uploader-btn :directory="true">select folder</uploader-btn>
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
            height: 257px;
            max-height: 257px;
            overflow: auto;
            overflow-x: hidden;
        }
    }
</style>