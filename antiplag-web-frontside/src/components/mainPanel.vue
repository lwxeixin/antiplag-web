<template>
    <div id="mainPanel" class="mx-auto pt-5 rounded">
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
                                    <option v-for="item in langOptions[compareTool]" :value="item" :key="'lang:' + compareTool + '-' + item">{{item==='doc'?'文档':item}}</option>
                                </select>
                            </div>
                        </div>
                        <template v-if="compareTool === 'jplag'">
                            <label for="range"/>
                            <input v-model="simValue" type="range" class="custom-range" min="1" max="100" step="1" id="range">
                        </template>
                        <template v-else>
                            <label for="range_disabled"/>
                            <input v-model="simValue" type="range" class="custom-range" min="1" max="100" step="1" id="range_disabled" disabled>
                        </template>
                        <div class="submit px-3 d-flex justify-content-between">
                            <button v-if="compareTool === 'jplag'" class="only-show btn btn-sm btn-outline-dark col-6 no-box-shadow">相似阈值 <span class="badge badge-light text-danger font-italic font-weight-bold">{{simValue}}</span></button>
                            <button v-else class="only-show btn btn-sm btn-dark col-6 no-box-shadow">相似阈值 <span class="badge badge-light text-danger font-italic font-weight-bold" disabled="">NAN</span></button>
                            <button v-if="['SIM', 'singleCloud'].indexOf(compareTool) !== -1" class="btn btn-outline-warning btn-sm no-box-shadow">暂不支持{{compareTool}}</button>
                            <button v-else-if="uploadedFilesNameList.length < 2" class="btn btn-outline-warning btn-sm no-box-shadow">请上传至少两个文件</button>
                            <button v-else-if="!submitting" @click="submit" type="button" class="btn btn-outline-success btn-sm no-box-shadow">执行比较</button>
                            <button v-else type="button" class="btn btn-outline-success btn-sm no-box-shadow" disabled>请等待片刻。。。</button>
                        </div>
                    </div>
                    <div class="result px-3 py-1 bg-light pre-scrollable text-nowrap">
                        <template v-if="!submitting">
                            <template v-if="resultType === 'jplag'">
                                <template v-if="result.length === 1">
                                    <p class="text-danger">请确认上传的文件类型与所选参数是否一致!</p>
                                    <p class="text-danger">请确认上传的文件类型具有有效内容!</p>
                                </template>
                                <template v-else>
                                    <p class="text-info">Matches sorted by average similarity <a href="https://jplag.ipd.kit.edu/example/help-sim-en.html" target="_blank">(What is this?)</a>:</p>
                                    <pre>{{result[0]}}</pre>
                                    <hr>
                                    <p class="text-info">Matches sorted by maximum similarity <a href="https://jplag.ipd.kit.edu/example/help-sim-en.html" target="_blank">(What is this?)</a>:</p>
                                    <pre>{{result[1]}}</pre>
                                </template>
                            </template>
                            <template v-else-if="resultType === 'MOSS'">
                                <template v-if="result.startsWith('http://moss.stanford.edu/results/')">
                                    查询成功!<br/>
                                    点击 <a :href="result" target="_blank">此链接</a> 查看结果
                                </template>
                                <template v-else>
                                    查询失败!<br/>
                                    {{result}}
                                </template>
                            </template>
                        </template>
                    </div>
                    <div class="operation d-flex justify-content-sm-end border-top bg-light">
                        <button v-if="!submitting && uploadedFilesNameList.length > 1 && resultType==='jplag' && result.length === 2" v-clipboard:copy="'Matches sorted by average similarity:\n'+result[0]+'\nMatches sorted by maximum similarity:\n'+result[1]" type="button" class="btn btn-light btn-sm no-box-shadow py-0">复制摘要到剪贴板</button>
                        <button v-else type="button" class="btn btn-light btn-sm no-box-shadow py-0" disabled>复制摘要到剪贴板</button>
                        <span>|</span>
                        <a v-if="!submitting && uploadedFilesNameList.length > 1 && resultType === 'jplag' && result.length === 2" :href="this.host + '/result/jplag'" type="button" class="btn btn-light btn-sm no-box-shadow py-0">保存详细结果到本地</a>
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
                        <div class="input-group input-group-sm mb-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="inputGroup-sizing-sm">请输入MOSS系统用户id</span>
                            </div>
                            <input oninput="value=value.replace(/[^\d]/g,'')" v-model="MOSSid" type="text" class="form-control no-box-shadow" aria-label="Small" aria-describedby="inputGroup-sizing-sm">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">取消</button>
                        <button v-if="MOSSid != null" type="button" class="btn btn-outline-success btn-sm no-box-shadow" @click="submitMOSS">执行比较</button>
                        <button v-else type="button" class="btn btn-outline-success btn-sm no-box-shadow" @click="submitMOSS" disabled>执行比较</button>
                    </div>
                </div>
            </div>
        </div>

        <button type="button" @click="exit" class="btn btn-outline-secondary fixed-top border-left-0 no-box-shadow font-italic rounded-0 mt-5"><span class="font-weight-bold font-italic text-danger">X </span>exit</button>
        <button type="button" data-toggle="modal" data-target="#helpModalCenter" class="btn btn-outline-secondary fixed-top border-left-0 no-box-shadow font-italic rounded-0"><span class="font-weight-bold font-italic text-primary">? </span>help</button>
        <github-corner user-name="mooyyu" repository-name="antiplag-web"/>
        <bottom-footer/>
        <help/>
    </div>
</template>

<script>
    import githubCorner from "@/components/githubCorner";
    import bottomFooter from "@/components/bottomFooter";
    import uploaderComp from "@/components/uploaderComp";
    import help from "@/components/help";

    import 'bootstrap/dist/js/bootstrap.min';
    import $ from 'jquery';

    export default {
        name: 'mainPanel',
        components: {
            githubCorner, bottomFooter, uploaderComp, help
        },
        data() {
            return {
                submitting: false,
                simValue: 30,
                uploadedFilesNameList: [],
                result: [],
                resultType: null,
                uploading: false,
                compareTool: 'jplag',
                MOSSid: null,
                selectedLang: {
                    jplag: 'java',
                    SIM: 'java',
                    MOSS: 'java',
                    singleCloud: 'doc'
                },
                langOptions: {
                    jplag: ['java', 'c/c++', 'python3', 'text', 'doc'],
                    SIM: ['java', 'c'],
                    MOSS: ["c", "cc", "java", "ml", "pascal", "ada", "lisp", "schema", "haskell", "fortran", "ascii", "vhdl", "perl", "matlab", "python", "mips", "prolog", "spice", "vb", "csharp", "modula2", "a8086", "javascript", "plsql"],
                    singleCloud: ['doc']
                }
            }
        },
        mounted() {
            this.updateFilesName();
        },
        methods: {
            submit() {
                if (this.compareTool === 'MOSS') $('div#MOSSModalCenter').modal('show');
                else if (this.compareTool === 'jplag') this.submitJplag();
            },
            submitMOSS() {
                $('div#MOSSModalCenter').modal('hide');
                this.submitting = true;
                this.result = null;
                this.axios.get(this.host + '/performCompare/MOSS', {
                    params: {
                        lang: this.selectedLang[this.compareTool],
                        id: this.MOSSid
                    }
                }).then(res => {
                    this.result = res.data;
                    this.submitting = false;
                    this.resultType = 'MOSS';
                })
            },
            submitJplag() {
                this.submitting = true;
                this.result = [];
                this.axios.get(this.host + '/performCompare/jplag', {
                    params: {
                        lang: this.selectedLang[this.compareTool],
                        simValue: this.simValue
                    }
                }).then(res => {
                    this.result = res.data;
                    this.submitting = false;
                    this.resultType = 'jplag';
                })
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
            },
            exit() {
                this.axios.get(this.host + '/login/exit').finally(() => {
                    this.$parent.isLogin = false;
                })
            }
        }
    }
</script>

<style lang="less" scoped>
    .no-box-shadow:focus {
        box-shadow: none!important;
    }
    div#mainPanel {
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
