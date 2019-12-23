<template>
    <uploader :options="options" class="uploader p-3">
        <uploader-unsupport/>
        <uploader-drop>
            <span>Drop files here to upload or </span>
            <uploader-btn :attrs="attrs" class="mr-1">select files</uploader-btn>
            <uploader-btn :directory="true">select folder</uploader-btn>
        </uploader-drop>
        <uploader-list/>
    </uploader>
</template>

<script>
    export default {
        name: "uploaderComp",
        data () {
            return {
                options: {
                    target: '//localhost:3000/upload',
                    testChunks: false
                },
                attrs: {
                    accept: 'application/zip'
                }
            }
        }
    }
</script>

<style lang="less" scoped>
    .uploader {
        font-size: small;
        .uploader-list {
            max-height: 440px;
            overflow: auto;
            overflow-x: hidden;
        }
    }
</style>