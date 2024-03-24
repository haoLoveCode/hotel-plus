<template>
  <div>
    <template v-for="item in routes">
      <div v-if="item.showOnly">
        <template
          v-for="child in item.children"
          v-if="!child.hidden"
        >
          <router-link
            class="menu-indent"
            :to="item.path + '/' + child.path"
          >
            <el-menu-item :index="item.path + '/' + child.path">
              <i
                v-if="item.icon"
                :class="item.icon"
              ></i>
              <span slot="title" class="menu-item">
                {{ child.name }}
              </span>
            </el-menu-item>
          </router-link>
        </template>
      </div>
      <div v-else>
        <el-submenu
          :index="item.name"
          v-if="!item.noDropdown && !item.hidden"
          :key="item.name"
        >
          <template slot="title">
            <i
              v-if="item.icon"
              :class="item.icon"
            ></i>
            <span slot="title" class="menu-item">{{ item.name }}</span>
          </template>
          <template
            v-for="child in item.children"
            v-if="!child.hidden"
          >
            <sidebar-item
              class="menu-indent"
              v-if="child.children && child.children.length > 0"
              :routes="[child]"
              :key="child.name"
            >
            </sidebar-item>
            <router-link
              v-else
              class="menu-indent"
              :to="item.path + '/' + child.path"
              :key="child.name"
            >
              <el-menu-item :index="item.path + '/' + child.path">
                <i
                  v-if="child.icon"
                  :class="child.icon"
                ></i>
                <span slot="title" class="menu-item">
                  {{ child.name }}
                </span>
              </el-menu-item>
            </router-link>
          </template>
        </el-submenu>
      </div>
    </template>
  </div>
</template>

<script>
  export default {
    name: "SidebarItem",
    props: {
      routes: {
        type: Array,
      },
    },
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.menu-item{
  display: inline-block;
  white-space:normal;
  word-break:break-all;
  max-width:150px;
  line-height:20px;
  font-weight: bold;
}
.svg-icon {
  margin-right: 10px;
}

.hideSidebar .menu-indent {
  display: block;
  text-indent: 10px;
}
</style>

