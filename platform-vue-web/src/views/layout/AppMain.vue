<template>
  <div class="app-main">
    <section>
      <transition name="fade" mode="out-in">
        <router-view :key="key" keep-alive></router-view>
      </transition>
    </section>
  </div>
</template>

<script>
	export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
    }
  }
}
</script>
<style lang="scss" scoped>
  .app-main{
    min-height: 100%;
    margin-left: 20px;
  }
</style>
