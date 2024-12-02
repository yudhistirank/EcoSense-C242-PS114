const {
  addArticleHandler,
  getAllArticlesHandler,
  getArticleByIdHandler,
  editArticleByIdHandler,
  deleteArticleByIdHandler
} = require('./handler');

const routes = [
  {
    method: 'POST',
    path: '/articles',
    handler: addArticleHandler,
    options: {
      payload: {
        maxBytes: 10 * 1024 * 1024,
        output: 'stream',
        parse: true,
        allow: ['application/json', 'multipart/form-data'],
        multipart: true,
        defaultContentType: 'application/json',
      },
    },
  },
  {
    method: 'GET',
    path: '/articles',
    handler: getAllArticlesHandler,
  },
  {
    method: 'GET',
    path: '/articles/{id}',
    handler: getArticleByIdHandler,
  },
  {
    method: 'PUT',
    path: '/articles/{id}',
    handler: editArticleByIdHandler,
    options: {
      payload: {
        maxBytes: 10 * 1024 * 1024,
        output: 'stream',
        parse: true,
        allow: ['application/json', 'multipart/form-data'],
        multipart: true,
        defaultContentType: 'application/json',
      },
    },
  },
  {
    method: 'DELETE',
    path: '/articles/{id}',
    handler: deleteArticleByIdHandler,
    options: {
      payload: {
        maxBytes: 10 * 1024 * 1024,
        output: 'stream',
        parse: true,
        allow: ['application/json', 'multipart/form-data'],
        multipart: true,
        defaultContentType: 'application/json',
      },
    },
  },
];

module.exports = routes;