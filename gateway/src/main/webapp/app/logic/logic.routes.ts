import { Routes } from '@angular/router';

const routes: Routes = [
  
  {
    path: 'upload',
    title: 'gatewayApp.documentServiceDocument.home.title' ,
    loadChildren: () => import('./upload/upload.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
