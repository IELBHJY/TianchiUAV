#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 29 15:17:11 2017

@author: apple
"""
import pandas as pd
import xgboost as xgb
from sklearn.cross_validation import train_test_split
df=pd.read_csv('/Users/apple/Documents/data/In-situMeasurementforTraining_20171124.csv')
X=df[['xid','yid','date_id','hour']]
y=df['wind']
for i in range(0,len(y)):
    if y[i]<15:
        y[i]=0
    else:
        y[i]=1
X_train,X_test,y_train,y_test=train_test_split(X,y,test_size=0.3,random_state=0)
dtrain=xgb.DMatrix(data=X_train,label=y_train)
Trate=0.25  
params = {'booster':'gbtree',
                  'eta': 0.1,
                   'max_depth': 4,
                   'max_delta_step': 0,
                   'subsample':0.9,      
                   'colsample_bytree': 0.9,
                   'base_score': Trate,
                   'objective': 'binary:logistic',
                   'lambda':5,
                   'alpha':8,
                   'random_seed':100
                   }
params['eval_metric'] = 'auc' 
xgb_model=xgb.train(params,dtrain,num_boost_round=200,maximize=True,verbose_eval=True)
y_pre=xgb_model.predict(xgb.DMatrix(X_test))
from sklearn.metrics import accuracy_score
print(accuracy_score(y_test,y_pre))
