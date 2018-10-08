import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);

exports.sendPushWhenAddCardRequest = functions.firestore
  .document('request/{requestId}')
  .onCreate((snap, context) => {

    const request = snap.data();
    const toUuid = request.toUuid
    const fromUuid = request.fromUuid

    return admin.auth().getUser(fromUuid)
      .then((userFrom) => {
        console.log('User has fetched');

        const payload = {
          data: {
            //token: token
          },
          notification: {
            title: "Title",
            body: "Description",
            sound: "default"
          },
        };

        const options = {
          priority: "high",
          timeToLive: 60 * 60 * 24
        };

        const notificationRef = admin.firestore().collection('notification').doc()
        const notification = {
          id: notificationRef.id,
          type: "ADD_CARD",
          title: userFrom.displayName + 'wants add your card',
          description: "Description",
          card: request.card
        };

        const saveNotificationPromise = notificationRef.set(notification)
        const sendPushPromise = admin.firestore().collection("token").where('userUuid', '==', toUuid).limit(1).get()
          .then(token => admin.messaging().sendToDevice(token.docs[0].data().token, payload, options))

        return Promise.all([saveNotificationPromise, sendPushPromise])
          .then(results => {
            console.log('Notification has added');
            console.log('Push has sent');
          })
          .catch(error => {
            console.log('Send notification error: ', error);
          });
      });
  });