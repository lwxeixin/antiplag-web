<template>
    <div class="modal fade" id="helpModalCenter" tabindex="-1" role="dialog" aria-labelledby="helpModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="helpModalCenterTitle">欢迎使用本系统</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-light" role="alert">
                        <p>网页版作业查重系统(antiplag web version)。现使用jplag和moss两套工具，未来计划支持singleCloud和SIM工具。</p>
                        <hr>
                        <ul class="ml-4">
                            <li>文件管理
                                <ul class="ml-4">
                                    <li>左侧为文件上传部分，支持多文件上传、文件夹上传方式，支持取消、断点续传、失败重试操作。可进行多次上传操作。</li>
                                    <li>中间为管理已上传到服务器文件部分。</li>
                                    <li>如果删除文件后需要重新上传同一文件，请先清除左侧记录。</li>
                                </ul>
                            </li>
                            <li>jplag
                                <ul class="ml-4">
                                    <li>jplag支持程序代码和文档的查重，可设置相似度阈值。</li>
                                    <li>jplag查重返回文档相似度摘要，可手动下载详细查重信息。</li>
                                    <li>对于文档，将先提取文本信息，返回文件名为"原文件名+.txt"的查重结果。</li>
                                </ul>
                            </li>
                            <li>MOSS
                                <ul class="ml-4">
                                    <li>使用MOSS工具需提供MOSS系统的用户id。</li>
                                    <li>使用MOSS查重将返回一个URL，结果保存在MOSS的服务器中，维持一周。</li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-dark btn-sm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "help"
    }
</script>

<style lang="less" scoped>
    .no-box-shadow:focus {
        box-shadow: none!important;
    }
</style>