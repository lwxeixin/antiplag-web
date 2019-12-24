<template>
    <div id="app" class="mx-auto pt-5 rounded">
        <div class="main">
            <div class="alert alert-success text-center" role="alert">
                <h4 class="alert-heading">代码、文件查重系统</h4>
                <p>antiplag (web version)</p>
                <hr>
            </div>
            <div class="main-body row px-3">
                <uploader-comp class="col-4"/>
                <div class="list p-3 col-4">
                    <ul class="nav bg-light">
                        <li class="nav-item w-100">
                            <div class="alert alert-light m-0 text-dark border-bottom d-flex justify-content-between rounded-0" role="alert">
                                <span>管理已上传文件</span>
                                <span>
                                    <button @click="updateFilesName" type="button" class="btn btn-outline-primary btn-sm py-0">刷新</button>
                                    <button v-if="uploadedFilesNameList !== null && uploadedFilesNameList.length" @click="deleteAllFiles" type="button" class="btn btn-outline-danger btn-sm py-0 ml-1">全部清除</button>
                                </span>
                            </div>
                        </li>
                    </ul>
                    <div class="list-folder bg-light">
                        <ul class="list-group list-group-flush">
                            <li v-for="(item, index) in uploadedFilesNameList" :key="'item-in-listFolder'+index" class="list-group-item list-group-item-action d-flex justify-content-between">
                                <span class="w-75 d-inline-block"><span class="font-weight-bold font-italic">{{index}}: </span>{{item}}</span>
                                <button @click="deleteFile(item)" type="button" class="btn btn-outline-danger btn-sm py-0">✕</button>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="form p-3 col-4">
                    <small class="form-text text-muted">
                        请上传一个不加密的zip压缩文件，待比较的文件应在压缩文件的根目录位置。
                    </small>
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">上传压缩文件</span>
                        </div>
                        <div class="custom-file">
                            <input type="file" accept="application/zip" class="custom-file-input" id="inputGroupFile01">
                            <label class="custom-file-label" for="inputGroupFile01"/>
                        </div>
                    </div>
                    <div class="form-radio">
                        <span>待比较文件类型: </span>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" checked>
                            <label class="form-check-label" for="inlineRadio1">代码文件</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2">
                            <label class="form-check-label" for="inlineRadio2">文本文件</label>
                        </div>
                    </div>
                    <div>
                        <label class="mt-2 mb-0" for="range">设置相似度阈值: <span class="text-danger font-weight-bold font-italic">{{simValue}}</span></label>
                        <input v-model="simValue" type="range" class="custom-range" min="0" max="100" step="1" id="range">
                    </div>
                    <div class="row">
                        <div class="input-group mb-3 col-6">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="inputGroupSelect01">检测工具</label>
                            </div>
                            <select class="custom-select" id="inputGroupSelect01">
                                <option value="1" selected>MOSS</option>
                                <option value="2">SIM</option>
                                <option value="3">jplag</option>
                            </select>
                        </div>
                        <div class="input-group mb-3 col-6">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="inputGroupSelect02">程序语言</label>
                            </div>
                            <select class="custom-select" id="inputGroupSelect02">
                                <option value="1" selected>One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                    </div>
                    <div class="submit w-100">
                        <button v-if="!submitting" @click="submit" type="button" class="btn btn-outline-success btn-sm mx-auto d-block">执行比较</button>
                        <button v-else type="button" class="btn btn-outline-success btn-sm mx-auto d-block" disabled>请等待片刻。。。</button>
                    </div>
                </div>
            </div>
        </div>

        <github-corner user-name="mooyyu" repository-name="antiplag-web"/>
        <bottom-footer/>
    </div>
</template>

<script>
    import githubCorner from "@/components/githubCorner";
    import bottomFooter from "@/components/bottomFooter";
    import uploaderComp from "@/components/uploaderComp";

    export default {
        name: 'app',
        components: {
            githubCorner, bottomFooter, uploaderComp
        },
        data() {
            return {
                submitting: false,
                simValue: 30,
                uploadedFilesNameList: null
            }
        },
        mounted() {
            this.updateFilesName();
        },
        methods: {
            submit() {
                let url = '/getFilesName';
                this.axios.get(this.host + url).then(res => {
                    // eslint-disable-next-line no-console
                    console.info(res.data);
                })
            },
            updateFilesName() {
                this.axios.get(this.host + '/getFilesName').then(res => {
                    this.uploadedFilesNameList = res.data;
                })
            },
            deleteFile(fileName) {
                this.axios.get(this.host + '/deleteFile', {
                    params: {
                        fileName: fileName
                    }
                }).then(res => {
                    if (res.data) this.updateFilesName();
                })
            },
            deleteAllFiles() {
                this.axios.get(this.host + '/deleteAllFiles').then(() => {
                    this.updateFilesName();
                })
            }
        }
    }
</script>

<style lang="less">
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        user-select: none;
    }
    div#app {
        width: 90vw;
        min-width: 980px;
        min-height: 300px;
        div.main {
            background-color: #c3e6cb;
            div.main-body {
                min-height: 390px;
                div.list {
                    div.list-folder {
                        height: 300px;
                        max-height: 300px;
                        overflow-y: scroll;
                        ul.list-group {
                            li.list-group-item {
                                span {
                                    white-space: nowrap;
                                    text-overflow: ellipsis;
                                    overflow: hidden;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
</style>
