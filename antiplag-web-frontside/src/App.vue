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
                                    <button v-if="!submitting" @click="updateFilesName" type="button" class="btn btn-outline-primary btn-sm py-0 no-box-shadow">刷新</button>
                                    <button v-else type="button" class="btn btn-outline-primary btn-sm py-0 no-box-shadow" disabled>刷新</button>
                                    <button v-if="!submitting && uploadedFilesNameList !== null && uploadedFilesNameList.length" @click="deleteAllFiles" type="button" class="btn btn-outline-danger btn-sm py-0 ml-1 no-box-shadow">全部清除</button>
                                    <button v-else type="button" class="btn btn-outline-danger btn-sm py-0 ml-1 no-box-shadow" disabled>全部清除</button>
                                </span>
                            </div>
                        </li>
                    </ul>
                    <div class="list-folder bg-light">
                        <ul class="list-group list-group-flush">
                            <li v-for="(item, index) in uploadedFilesNameList" :key="'item-in-listFolder'+index" class="list-group-item list-group-item-action d-flex justify-content-between">
                                <span class="w-75 d-inline-block"><span class="font-weight-bold font-italic">{{index}}: </span>{{item}}</span>
                                <button v-if="!submitting" @click="deleteFile(item)" type="button" class="btn btn-outline-danger btn-sm py-0 no-box-shadow">✕</button>
                                <button v-else type="button" class="btn btn-outline-danger btn-sm py-0 no-box-shadow" disabled>✕</button>
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
                                <select v-model="compareTool" class="custom-select no-box-shadow" id="inputGroupSelect01">
                                    <option value="jplag">jplag</option>
                                    <option value="SIM">SIM</option>
                                    <option value="MOSS">MOSS</option>
                                    <option value="singleCloud">singleCloud</option>
                                </select>
                            </div>
                            <div class="input-group col-6 input-group-sm">
                                <div class="input-group-prepend">
                                    <label class="input-group-text" for="inputGroupSelect02">语言</label>
                                </div>
                                <select v-model="selectedLang[compareTool]" class="custom-select no-box-shadow" id="inputGroupSelect02">
                                    <option v-for="item in langOptions[compareTool]" :value="item" :key="'lang:' + compareTool + '-' + item">{{item}}</option>
                                </select>
                            </div>
                        </div>
                        <label for="range"/>
                        <input v-model="simValue" type="range" class="custom-range" min="0" max="100" step="1" id="range">
                        <div class="submit px-3 d-flex justify-content-between">
                            <button class="only-show btn btn-sm btn-outline-dark col-6 no-box-shadow">相似阈值 <span class="badge badge-light text-danger font-italic font-weight-bold">{{simValue}}</span></button>
                            <button v-if="uploadedFilesNameList.length < 2" class="btn btn-outline-warning btn-sm no-box-shadow">请上传至少两个文件</button>
                            <button v-else-if="!submitting" @click="submit" type="button" class="btn btn-outline-success btn-sm no-box-shadow">执行比较</button>
                            <button v-else type="button" class="btn btn-outline-success btn-sm no-box-shadow" disabled>请等待片刻。。。</button>
                        </div>
                    </div>
                    <div class="result px-3 py-1 bg-light pre-scrollable">
                        <pre>{{result}}</pre>
                    </div>
                    <div class="operation d-flex justify-content-sm-end border-top bg-light">
                        <button v-if="!submitting && uploadedFilesNameList.length > 1" type="button" class="btn btn-light btn-sm no-box-shadow py-0">复制摘要到剪贴板</button>
                        <button v-else type="button" class="btn btn-light btn-sm no-box-shadow py-0" disabled>复制摘要到剪贴板</button>
                        <span>|</span>
                        <a v-if="!submitting && uploadedFilesNameList.length > 1" :href="this.host + '/result/jplag'" type="button" class="btn btn-light btn-sm no-box-shadow py-0">保存详细结果到本地</a>
                        <button v-else type="button" class="btn btn-light btn-sm no-box-shadow py-0" disabled>保存详细结果到本地</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="MOSSModalCenter" tabindex="-1" role="dialog" aria-labelledby="MOSSModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="MOSSModalCenterTitle">你选择了使用MOSS系统</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        ...
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-outline-success btn-sm no-box-shadow" @click="submitMOSS">执行比较</button>
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

    import 'bootstrap/dist/js/bootstrap.min';
    import $ from 'jquery';

    export default {
        name: 'app',
        components: {
            githubCorner, bottomFooter, uploaderComp
        },
        data() {
            return {
                submitting: false,
                simValue: 30,
                uploadedFilesNameList: [],
                result: null,
                uploading: false,
                compareTool: 'jplag',
                MOSSid: null,
                selectedLang: {
                    jplag: 'java',
                    SIM: 'java',
                    MOSS: 'java',
                    singleCloud: 'plainText'
                },
                langOptions: {
                    jplag: ['java', 'c/c++', 'plainText'],
                    SIM: ['java', 'c/c++'],
                    MOSS: ['java', 'c/c++', 'python'],
                    singleCloud: ['plainText']
                }
            }
        },
        mounted() {
            this.updateFilesName();
        },
        methods: {
            submit() {
                if (this.compareTool === 'MOSS') {
                    $('div#MOSSModalCenter').modal('show');
                } else {
                    this.submitting = true;
                    this.axios.get(this.host + '/performCompare/' + this.compareTool, {
                        params: {
                            lang: this.selectedLang[this.compareTool],
                            simValue: this.simValue
                        }
                    }).then(res => {
                        this.result = res.data;
                        this.submitting = false;
                    })
                }
            },
            submitMOSS() {
                $('div#MOSSModalCenter').modal('hide');
            },
            updateFilesName() {
                this.axios.get(this.host + '/fileManager/getFilesName').then(res => {
                    this.uploadedFilesNameList = res.data;
                })
            },
            deleteFile(fileName) {
                this.axios.get(this.host + '/fileManager/deleteFile', {
                    params: {
                        fileName: fileName
                    }
                }).then(res => {
                    if (res.data) this.updateFilesName();
                })
            },
            deleteAllFiles() {
                this.axios.get(this.host + '/fileManager/deleteAllFiles').then(() => {
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
