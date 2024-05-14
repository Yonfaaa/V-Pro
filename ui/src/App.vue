<template>
  <div id="app">
    <router-view />
    <theme-picker />
<!--  <TestComponent/>-->
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";
import TestComponent from "@/components/test.vue";
import test from "@/components/test.vue";


export default {
  name: "App",
  components: {TestComponent, ThemePicker },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
