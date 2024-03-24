import {mapGetters} from 'vuex';
export default {
  data() {
    return {

    }
  },
    computed: {
        ...mapGetters([
            'sidebar',
            'userName',
            'avatar',
            'groupType',
            'btnCode'
        ])
    },
  methods: {
      getUserPermission() {
          if (!this.userName){

          }
      }
  },
  created(){
      this.getUserPermission()
  },
}
