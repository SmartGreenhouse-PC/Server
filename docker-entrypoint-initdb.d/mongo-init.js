db = db.getSiblingDB('greenhouse');

db.createCollection('greenhouse');

db.greenhouse.insertMany( [
{
  _id: ObjectId('63af0ae025d55e9840cbc1fc'),
  id: "greenhouse1",
  plant: {
    name: 'Peperoncino rosso calabrese',
    description: 'È un arbusto a portamento eretto, ha foglie lanceolate, verdi. Può essere presente una peluria su fusto e pagina superiore delle foglie. I fiori, bianchi, singoli, con stami giallo/verdi, a 5-7 petali, compaiono all\'ascella delle foglie, uno per nodo, in estate.',
    img: 'http://www.peperoncinodicalabria.it/wp-content/uploads/2015/08/naso-di-cane-peperoncino-calabrese.jpg',
    parameters: [
        { name: 'temperature',
          min: 10.0,
          max: 35.0,
          unit: '℃'
        },
        { name: 'brightness',
          min: 3000.0,
          max: 95000.0,
          unit: 'Lux'
        },
        { name: 'soilMoisture',
          min: 20.0,
          max: 65.0,
          unit: '%'
        },
        { name: 'humidity',
          min: 30.0,
          max: 80.0,
          unit: '%'
        }
    ]
  },
  modality: 'AUTOMATIC'
},
{
  _id: ObjectId('63af0ae025d55e9840cbc1fa'),
  id: "greenhouse2",
  plant: {
    name: 'lemon',
    description: 'is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.',
    img: 'http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg',
    parameters: [
            { name: 'temperature',
              min: 8.0,
              max: 35.0,
              unit: '℃'
            },
            { name: 'brightness',
              min: 4200.0,
              max: 130000.0,
              unit: 'Lux'
            },
            { name: 'soilMoisture',
              min: 20.0,
              max: 65.0,
              unit: '%'
            },
            { name: 'humidity',
              min: 30.0,
              max: 80.0,
              unit: '%'
            }
        ]
  },
  modality: 'AUTOMATIC'
},
{
  _id: ObjectId('63b29b0a3792e15bae3229a7'),
  id: "greenhouse3",
  plant: {
    name: 'lemon AUTOMATIC',
    description: 'is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.',
    img: 'http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg',
    parameters: [
            { name: 'temperature',
              min: 8.0,
              max: 35.0,
              unit: '℃'
            },
            { name: 'brightness',
              min: 4200.0,
              max: 130000.0,
              unit: 'Lux'
            },
            { name: 'soilMoisture',
              min: 20.0,
              max: 65.0,
              unit: '%'
            },
            { name: 'humidity',
              min: 30.0,
              max: 80.0,
              unit: '%'
            }
        ]
  },
  modality: 'AUTOMATIC'
}
]);
