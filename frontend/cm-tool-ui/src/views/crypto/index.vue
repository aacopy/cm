<script setup lang="ts">
import { reactive, ref } from 'vue';
import { fetchCryptoProcess, fetchCryptoRsaGenerateKeyPair } from '@/service/api';

type TabKey = 'md5' | 'base64' | 'url' | 'aes' | 'rsa' | 'rsa-keygen';

const activeTab = ref<TabKey>('md5');

// MD5
const md5Form = reactive({
  input: ''
});
const md5Result = ref('');
const md5Loading = ref(false);

// Base64
const base64Form = reactive({
  input: '',
  mode: 'ENCRYPT' as Api.Crypto.CryptoMode
});
const base64Result = ref('');
const base64Loading = ref(false);

// URL
const urlForm = reactive({
  input: '',
  mode: 'ENCRYPT' as Api.Crypto.CryptoMode
});
const urlResult = ref('');
const urlLoading = ref(false);

// AES
const aesForm = reactive({
  input: '',
  key: '',
  mode: 'ENCRYPT' as Api.Crypto.CryptoMode
});
const aesResult = ref('');
const aesLoading = ref(false);

// RSA
const rsaForm = reactive({
  input: '',
  key: '',
  mode: 'ENCRYPT' as Api.Crypto.CryptoMode
});
const rsaResult = ref('');
const rsaLoading = ref(false);

// RSA KeyGen
const rsaKeyGenForm = reactive({
  keySize: 2048
});
const rsaKeyGenResult = reactive({
  publicKey: '',
  privateKey: ''
});
const rsaKeyGenLoading = ref(false);

function handleCopy(text: string, label = '结果') {
  if (!text) return;

  navigator.clipboard
    .writeText(text)
    .then(() => window.$message?.success(`${label}已复制到剪贴板`))
    .catch(() => window.$message?.error('复制失败，请检查浏览器权限'));
}

async function handleMd5() {
  if (!md5Form.input) {
    window.$message?.warning('请输入内容');
    return;
  }

  md5Loading.value = true;
  try {
    const { data, error } = await fetchCryptoProcess({
      type: 'MD5',
      input: md5Form.input
    });
    if (error) throw error;
    md5Result.value = String(data);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    md5Loading.value = false;
  }
}

async function handleBase64() {
  if (!base64Form.input) {
    window.$message?.warning('请输入内容');
    return;
  }

  base64Loading.value = true;
  try {
    const { data, error } = await fetchCryptoProcess({
      type: 'BASE64',
      mode: base64Form.mode,
      input: base64Form.input
    });
    if (error) throw error;
    base64Result.value = String(data);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    base64Loading.value = false;
  }
}

async function handleUrl() {
  if (!urlForm.input) {
    window.$message?.warning('请输入内容');
    return;
  }

  urlLoading.value = true;
  try {
    const { data, error } = await fetchCryptoProcess({
      type: 'URL',
      mode: urlForm.mode,
      input: urlForm.input
    });
    if (error) throw error;
    urlResult.value = String(data);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    urlLoading.value = false;
  }
}

async function handleAes() {
  if (!aesForm.input) {
    window.$message?.warning('请输入内容');
    return;
  }
  if (!aesForm.key) {
    window.$message?.warning('请输入密钥');
    return;
  }

  aesLoading.value = true;
  try {
    const { data, error } = await fetchCryptoProcess({
      type: 'AES',
      mode: aesForm.mode,
      input: aesForm.input,
      key: aesForm.key
    });
    if (error) throw error;
    aesResult.value = String(data);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    aesLoading.value = false;
  }
}

async function handleRsa() {
  if (!rsaForm.input) {
    window.$message?.warning('请输入内容');
    return;
  }
  if (!rsaForm.key) {
    window.$message?.warning('请输入密钥');
    return;
  }

  rsaLoading.value = true;
  try {
    const { data, error } = await fetchCryptoProcess({
      type: 'RSA',
      mode: rsaForm.mode,
      input: rsaForm.input,
      key: rsaForm.key
    });
    if (error) throw error;
    rsaResult.value = String(data);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    rsaLoading.value = false;
  }
}

async function handleRsaKeyGen() {
  rsaKeyGenLoading.value = true;
  try {
    const { data, error } = await fetchCryptoRsaGenerateKeyPair(rsaKeyGenForm.keySize);
    if (error) throw error;
    rsaKeyGenResult.publicKey = String(data?.publicKey || '');
    rsaKeyGenResult.privateKey = String(data?.privateKey || '');
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    rsaKeyGenLoading.value = false;
  }
}
</script>

<template>
  <NSpace vertical :size="16">
    <NCard :bordered="false" class="card-wrapper">
      <NTabs v-model:value="activeTab" type="line" animated>
        <!-- MD5 -->
        <NTabPane name="md5" tab="MD5">
          <NForm label-placement="top">
            <NFormItem label="输入内容">
              <NInput
                v-model:value="md5Form.input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                placeholder="请输入要加密的内容"
              />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="md5Loading" @click="handleMd5">生成 MD5</NButton>
              <NButton secondary :disabled="!md5Result" @click="handleCopy(md5Result)">复制结果</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px text-14px text-gray-500">结果</div>
            <NInput v-model:value="md5Result" readonly placeholder="MD5 结果将显示在这里" />
          </div>
        </NTabPane>

        <!-- Base64 -->
        <NTabPane name="base64" tab="Base64">
          <NForm label-placement="top">
            <NFormItem label="模式">
              <NRadioGroup v-model:value="base64Form.mode">
                <NRadio value="ENCRYPT">加密</NRadio>
                <NRadio value="DECRYPT">解密</NRadio>
              </NRadioGroup>
            </NFormItem>
            <NFormItem label="输入内容">
              <NInput
                v-model:value="base64Form.input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                :placeholder="base64Form.mode === 'ENCRYPT' ? '请输入要加密的内容' : '请输入要解密的 Base64 字符串'"
              />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="base64Loading" @click="handleBase64">
                {{ base64Form.mode === 'ENCRYPT' ? '加密' : '解密' }}
              </NButton>
              <NButton secondary :disabled="!base64Result" @click="handleCopy(base64Result)">复制结果</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px text-14px text-gray-500">结果</div>
            <NInput
              v-model:value="base64Result"
              type="textarea"
              readonly
              :autosize="{ minRows: 3, maxRows: 6 }"
              placeholder="结果将显示在这里"
            />
          </div>
        </NTabPane>

        <!-- URL -->
        <NTabPane name="url" tab="URL 编解码">
          <NForm label-placement="top">
            <NFormItem label="模式">
              <NRadioGroup v-model:value="urlForm.mode">
                <NRadio value="ENCRYPT">加密</NRadio>
                <NRadio value="DECRYPT">解密</NRadio>
              </NRadioGroup>
            </NFormItem>
            <NFormItem label="输入内容">
              <NInput
                v-model:value="urlForm.input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                :placeholder="urlForm.mode === 'ENCRYPT' ? '请输入要加密的 URL' : '请输入要解密的 URL'"
              />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="urlLoading" @click="handleUrl">
                {{ urlForm.mode === 'ENCRYPT' ? '加密' : '解密' }}
              </NButton>
              <NButton secondary :disabled="!urlResult" @click="handleCopy(urlResult)">复制结果</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px text-14px text-gray-500">结果</div>
            <NInput
              v-model:value="urlResult"
              type="textarea"
              readonly
              :autosize="{ minRows: 3, maxRows: 6 }"
              placeholder="结果将显示在这里"
            />
          </div>
        </NTabPane>

        <!-- AES -->
        <NTabPane name="aes" tab="AES">
          <NForm label-placement="top">
            <NFormItem label="模式">
              <NRadioGroup v-model:value="aesForm.mode">
                <NRadio value="ENCRYPT">加密</NRadio>
                <NRadio value="DECRYPT">解密</NRadio>
              </NRadioGroup>
            </NFormItem>
            <NFormItem label="密钥">
              <NInput v-model:value="aesForm.key" placeholder="请输入 AES 密钥" />
            </NFormItem>
            <NFormItem label="输入内容">
              <NInput
                v-model:value="aesForm.input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                :placeholder="aesForm.mode === 'ENCRYPT' ? '请输入要加密的内容' : '请输入要解密的密文'"
              />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="aesLoading" @click="handleAes">
                {{ aesForm.mode === 'ENCRYPT' ? '加密' : '解密' }}
              </NButton>
              <NButton secondary :disabled="!aesResult" @click="handleCopy(aesResult)">复制结果</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px text-14px text-gray-500">结果</div>
            <NInput
              v-model:value="aesResult"
              type="textarea"
              readonly
              :autosize="{ minRows: 3, maxRows: 6 }"
              placeholder="结果将显示在这里"
            />
          </div>
        </NTabPane>

        <!-- RSA -->
        <NTabPane name="rsa" tab="RSA">
          <NForm label-placement="top">
            <NFormItem label="模式">
              <NRadioGroup v-model:value="rsaForm.mode">
                <NRadio value="ENCRYPT">加密</NRadio>
                <NRadio value="DECRYPT">解密</NRadio>
              </NRadioGroup>
            </NFormItem>
            <NFormItem :label="rsaForm.mode === 'ENCRYPT' ? '公钥' : '私钥'">
              <NInput
                v-model:value="rsaForm.key"
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 8 }"
                :placeholder="rsaForm.mode === 'ENCRYPT' ? '请输入 RSA 公钥' : '请输入 RSA 私钥'"
              />
            </NFormItem>
            <NFormItem label="输入内容">
              <NInput
                v-model:value="rsaForm.input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                :placeholder="rsaForm.mode === 'ENCRYPT' ? '请输入要加密的内容' : '请输入要解密的密文'"
              />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="rsaLoading" @click="handleRsa">
                {{ rsaForm.mode === 'ENCRYPT' ? '加密' : '解密' }}
              </NButton>
              <NButton secondary :disabled="!rsaResult" @click="handleCopy(rsaResult)">复制结果</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px text-14px text-gray-500">结果</div>
            <NInput
              v-model:value="rsaResult"
              type="textarea"
              readonly
              :autosize="{ minRows: 3, maxRows: 6 }"
              placeholder="结果将显示在这里"
            />
          </div>
        </NTabPane>

        <!-- RSA 密钥对生成 -->
        <NTabPane name="rsa-keygen" tab="RSA 密钥对生成">
          <NForm label-placement="top">
            <NFormItem label="密钥长度">
              <NRadioGroup v-model:value="rsaKeyGenForm.keySize">
                <NRadio :value="1024">1024 位</NRadio>
                <NRadio :value="2048">2048 位</NRadio>
                <NRadio :value="4096">4096 位</NRadio>
              </NRadioGroup>
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="rsaKeyGenLoading" @click="handleRsaKeyGen">生成密钥对</NButton>
            </div>
          </NForm>
          <NDivider />
          <div>
            <div class="mb-12px flex items-center justify-between text-14px text-gray-500">
              <span>公钥</span>
              <NButton
                text
                :disabled="!rsaKeyGenResult.publicKey"
                @click="handleCopy(rsaKeyGenResult.publicKey, '公钥')"
              >
                <template #icon>
                  <NIcon><div class="i-carbon-copy" /></NIcon>
                </template>
              </NButton>
            </div>
            <NInput
              v-model:value="rsaKeyGenResult.publicKey"
              type="textarea"
              readonly
              :autosize="{ minRows: 6, maxRows: 10 }"
              placeholder="公钥将显示在这里"
            />
          </div>
          <div class="mt-16px">
            <div class="mb-12px flex items-center justify-between text-14px text-gray-500">
              <span>私钥</span>
              <NButton
                text
                :disabled="!rsaKeyGenResult.privateKey"
                @click="handleCopy(rsaKeyGenResult.privateKey, '私钥')"
              >
                <template #icon>
                  <NIcon><div class="i-carbon-copy" /></NIcon>
                </template>
              </NButton>
            </div>
            <NInput
              v-model:value="rsaKeyGenResult.privateKey"
              type="textarea"
              readonly
              :autosize="{ minRows: 6, maxRows: 10 }"
              placeholder="私钥将显示在这里"
            />
          </div>
        </NTabPane>
      </NTabs>
    </NCard>
  </NSpace>
</template>

<style scoped></style>
