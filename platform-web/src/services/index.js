import axios from './request';
import globalServices from './api';
const api = {};
const urlsAllThere = {};
api.getUrl = (key) => urlsAllThere[key].url;
export const generator = (urls) => {
  const mergedUrls = {
    ...globalServices,
    ...urls,
  };
  const handleUrl = (key) => {
    const {
      method, url, responseType
    } = mergedUrls[key];
    return {
      method,
      url,
      responseType,
    };
  };
  Object.keys(mergedUrls).forEach((item) => {
    urlsAllThere[item] = mergedUrls[item];
    const localItem = handleUrl(item);
    const { method } = localItem;
    switch (String(method).toUpperCase()) {
      case 'POST':
      case 'PUT':
      case 'PATCH':
        api[item] = (params) => axios(Object.assign({}, localItem, {
          data: params,
        }));
        break;
      default:
        api[item] = (params) => axios(Object.assign({}, localItem, {
          params,
        }));
    }
  });
  return api;
};
generator(globalServices);
export default api;
