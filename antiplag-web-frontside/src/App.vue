<template>
    <div id="app" class="mx-auto pt-5 rounded">
        <div class="main">
            <div class="alert alert-success text-center mb-0 pb-0" role="alert">
                <h4 class="alert-heading mb-0">代码、文件查重系统</h4>
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
                <div class="form-out p-3 col-4 ">
                    <div class="form px-3 py-2 bg-light border-bottom">
                        <div class="row mb-1">
                            <div class="input-group col-6 input-group-sm">
                                <div class="input-group-prepend">
                                    <label class="input-group-text" for="inputGroupSelect01">工具</label>
                                </div>
                                <select class="custom-select no-box-shadow" id="inputGroupSelect01">
                                    <option value="1" selected>MOSS</option>
                                    <option value="2">SIM</option>
                                    <option value="3">jplag</option>
                                </select>
                            </div>
                            <div class="input-group col-6 input-group-sm">
                                <div class="input-group-prepend">
                                    <label class="input-group-text" for="inputGroupSelect02">语言</label>
                                </div>
                                <select class="custom-select no-box-shadow" id="inputGroupSelect02">
                                    <option value="1" selected>One</option>
                                    <option value="2">Two</option>
                                    <option value="3">Three</option>
                                </select>
                            </div>
                        </div>
                        <label for="range"/>
                        <input v-model="simValue" type="range" class="custom-range" min="0" max="100" step="1" id="range">
                        <div class="submit row px-3">
                            <button class="only-show btn btn-sm btn-outline-dark col-4 no-box-shadow">相似阈值 <span class="badge badge-light text-danger font-italic font-weight-bold">{{simValue}}</span></button>
                            <div v-if="type" class="input-group input-group-sm col-4">
                                <div class="input-group-prepend">
                                    <button class="btn btn-primary" type="button">代码</button>
                                </div>
                                <div class="input-group-append">
                                    <button class="btn btn-outline-primary" @click="type = false" type="button">文本</button>
                                </div>
                            </div>
                            <div v-else class="input-group input-group-sm col-4">
                                <div class="input-group-prepend">
                                    <button class="btn btn-outline-primary" @click="type = true" type="button">代码</button>
                                </div>
                                <div class="input-group-append">
                                    <button class="btn btn-primary" type="button">文本</button>
                                </div>
                            </div>
                            <button v-if="!submitting" @click="submit" type="button" class="btn btn-outline-success btn-sm col-4 no-box-shadow">执行比较</button>
                            <button v-else type="button" class="btn btn-outline-success btn-sm col-4" disabled>请等待片刻。。。</button>
                        </div>
                    </div>
                    <div class="result px-3 py-1 bg-light">

                    </div>
                    <div class="operation d-flex justify-content-sm-end border-top bg-light">
                        <button type="button" class="btn btn-light btn-sm no-box-shadow py-0">复制到剪贴板</button>
                        <span>|</span>
                        <button type="button" class="btn btn-light btn-sm no-box-shadow py-0">保存到本地</button>
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
                uploadedFilesNameList: null,
                type: true
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
    .no-box-shadow:focus {
        box-shadow: none!important;
    }
    div#app {
        width: 90vw;
        min-width: 1296px;
        div.main {
            background-color: #c3e6cb;
            div.main-body {
                min-height: 390px;
                div.list {
                    div.list-folder {
                        height: 400px;
                        max-height: 400px;
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
                div.form-out {
                    div.form {
                        div.submit {
                            button.only-show {
                                cursor: default;
                            }
                        }
                    }
                    div.result {
                        height: 315px;
                    }
                }
            }
        }
    }
</style>
