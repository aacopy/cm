<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { fetchGenerateRandomString, fetchGenerateSnowflakeId, fetchGenerateUuid } from '@/service/api';

type TabKey = 'snowflake' | 'uuid' | 'random';

const activeTab = ref<TabKey>('snowflake');

const uuidForm = reactive({
  withHyphen: false
});

const randomForm = reactive({
  length: 6
});

const result = ref('');
const snowflakeLoading = ref(false);
const uuidLoading = ref(false);
const randomLoading = ref(false);

const resultPlaceholder = computed(() => {
  if (activeTab.value === 'snowflake') return '点击「生成」获取雪花 ID';
  if (activeTab.value === 'uuid') return '点击「生成」获取 UUID';
  return '点击「生成」通过接口获取随机字符串';
});

function handleCopy() {
  if (!result.value) return;

  navigator.clipboard
    .writeText(result.value)
    .then(() => window.$message?.success('已复制到剪贴板'))
    .catch(() => window.$message?.error('复制失败，请检查浏览器权限'));
}

async function handleGenerateSnowflake() {
  snowflakeLoading.value = true;
  try {
    const { data: id } = await fetchGenerateSnowflakeId();
    result.value = String(id);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    snowflakeLoading.value = false;
  }
}

async function handleGenerateUuid() {
  uuidLoading.value = true;
  try {
    const { data: id } = await fetchGenerateUuid(uuidForm.withHyphen);
    result.value = String(id);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    uuidLoading.value = false;
  }
}

async function handleGenerateRandom() {
  if (!randomForm.length || randomForm.length < 1) {
    window.$message?.warning('长度必须大于 0');
    return;
  }

  randomLoading.value = true;
  try {
    const { data: value } = await fetchGenerateRandomString(randomForm.length);
    result.value = String(value);
  } catch (e) {
    const msg = e instanceof Error ? e.message : '请求失败';
    window.$message?.error(msg);
  } finally {
    randomLoading.value = false;
  }
}
</script>

<template>
  <NSpace vertical :size="16">
    <NCard :bordered="false" class="card-wrapper">
      <NTabs v-model:value="activeTab" type="line" animated>
        <NTabPane name="snowflake" tab="雪花算法">
          <div class="flex gap-12px">
            <NButton type="primary" :loading="snowflakeLoading" @click="handleGenerateSnowflake">生成</NButton>
            <NButton secondary :disabled="!result" @click="handleCopy">复制</NButton>
          </div>
        </NTabPane>

        <NTabPane name="uuid" tab="UUID">
          <NForm label-placement="left" label-width="110">
            <NFormItem label="是否带 -">
              <NSwitch v-model:value="uuidForm.withHyphen" />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="uuidLoading" @click="handleGenerateUuid">生成</NButton>
              <NButton secondary :disabled="!result" @click="handleCopy">复制</NButton>
            </div>
          </NForm>
        </NTabPane>

        <NTabPane name="random" tab="随机字符串（接口）">
          <NForm label-placement="left" label-width="110">
            <NFormItem label="长度">
              <NInputNumber v-model:value="randomForm.length" :min="1" :max="64" class="w-full" />
            </NFormItem>
            <div class="flex gap-12px">
              <NButton type="primary" :loading="randomLoading" @click="handleGenerateRandom">生成</NButton>
              <NButton secondary :disabled="!result" @click="handleCopy">复制</NButton>
            </div>
          </NForm>
        </NTabPane>
      </NTabs>
    </NCard>

    <NCard :bordered="false" class="card-wrapper">
      <div class="mb-12px text-14px text-gray-500">结果</div>
      <NInput v-model:value="result" type="textarea" readonly :autosize="{ minRows: 3, maxRows: 6 }" :placeholder="resultPlaceholder" />
    </NCard>
  </NSpace>
</template>

<style scoped></style>

