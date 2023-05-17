import { Component, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { OsobaService } from './service/osoba.service';
import { BehaviorSubject, Observable, catchError, map, of, startWith } from 'rxjs';
import { FormGroup, NgForm } from '@angular/forms';
import { Osoba } from './interface/osoba';
import { OsobaVM } from './interface/osoba-vm';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  osobas: Osoba[];
  private isEditing = new BehaviorSubject<boolean>(false);
  isEditing$ = this.isEditing.asObservable();
  private file: File;
  private byteArray: any;

  /* https://www.tektutorialshub.com/angular/how-to-set-value-in-template-driven-forms-in-angular/ */
  @ViewChild('osobaForm') osobaForm: NgForm;
  @ViewChild('osobaVWForm') osobaVWForm: NgForm;

  constructor(private serverService: OsobaService) { }

  ngOnInit(): void {
    this.onGetOsobas();
    this.isEditing = new BehaviorSubject<boolean>(true);
    this.isEditing$ = this.isEditing.asObservable();

  }

  onGetOsobas(): void {
    this.serverService.getOsobas().subscribe({
      next: (response) => {
        this.osobas = response;
      },
      error: (error: any) => console.log(error),
      complete: () => console.log('Done getting users')
    });
  }

  onCreateUpdateOsoba(osobaForm: NgForm): void {
    let osoba_ = osobaForm.value as Osoba;
    if ((this.byteArray as string).includes("data:image/png;base64,")) {
      this.byteArray = (this.byteArray as string).substring(22, (this.byteArray as string).length);
    }
    osoba_.photoOfAPerson = this.byteArray;
    if (osoba_.id === null || osoba_.id === 0 || osoba_.id.toString() === '') {
      this.serverService.createOsoba(osoba_).subscribe({
        next: (response) => {
          this.osobas.push(response);
        },
        error: (error: any) => console.log(error),
        complete: () => console.log('Done creating osoba')
      });
    } else {
      this.serverService.updateOsoba(osoba_).subscribe({
        next: (response) => {
          this.osobas = this.osobas.map(item => {
            if (item.id === osoba_.id) {
              item.id = osoba_.id;
              item.name = osoba_.name;
              item.surname = osoba_.surname;
              item.homePhone = osoba_.homePhone;
              item.officePhone = osoba_.officePhone;
              item.email = osoba_.email;
              item.photoOfAPerson = 'data:image/png;base64,' + osoba_.photoOfAPerson;
            }
            return item;
          });
        },
        error: (error: any) => console.log(error),
        complete: () => console.log('Done updating osoba')
      });
    }
  }

  /* https://stackoverflow.com/questions/46241634/angular-get-the-data-of-the-selected-row-in-the-table */
  preFillOsobaForm(osoba: Osoba, editing: boolean) {
    this.osobaForm.controls["id"].setValue(osoba.id);
    this.osobaForm.controls["name"].setValue(osoba.name);
    this.osobaForm.controls["surname"].setValue(osoba.surname);
    this.osobaForm.controls["homePhone"].setValue(osoba.homePhone);
    this.osobaForm.controls["officePhone"].setValue(osoba.officePhone);
    this.osobaForm.controls["email"].setValue(osoba.email);
    this.isEditing = new BehaviorSubject<boolean>(editing);
    this.isEditing$ = this.isEditing.asObservable();

    if (osoba.id !== null) {
      previewImage(osoba.photoOfAPerson);
      this.byteArray = osoba.photoOfAPerson;
    }
  }

  reset() {
    this.osobaForm.reset();
    this.isEditing = new BehaviorSubject<boolean>(true);
    this.isEditing$ = this.isEditing.asObservable();
    let preview = document.querySelector('#preview');
    preview.innerHTML = "";
  }

  filterOsobas(osobaVWForm: NgForm): void {
    let osobaVM = osobaVWForm.value as OsobaVM;
    this.serverService.findOsobaFilter(osobaVM).subscribe({
      next: (response) => {
        this.osobas = response;
      },
      error: (error: any) => console.log(error),
      complete: () => console.log('Done filter osobas')
    });
  }

  onDeleteOsoba(osoba: Osoba, index): void {
    this.serverService.deleteOsoba(osoba.id).subscribe({
      next: (response) => {
        this.osobas.splice(index, 1);
        console.log('Response from delete: ', response)
      },
      error: (error: any) => console.log(error),
      complete: () => console.log('Done deleting osoba')
    });
  }

  async onUploadFile(files: File[]): Promise<void> {
    for (const file of files) {
      this.file = file;
    }
    this.byteArray = await fileToByteArray(this.file);
    readAndPreview(this.file);
  }

}

function fileToByteArray(file) {
  return new Promise((resolve, reject) => {
    try {
      let reader = new FileReader();
      let fileByteArray = [];
      reader.readAsArrayBuffer(file);
      reader.onloadend = (evt) => {
        if (evt.target.readyState === FileReader.DONE) {
          let arrayBuffer_ = evt.target.result;
          let array = new Uint8Array(arrayBuffer_ as ArrayBuffer);
          for (let byte of array) {
            fileByteArray.push(byte);
          }
        }
        resolve(fileByteArray);
      }
    }
    catch (e) {
      reject(e);
    }
  })
}

function readAndPreview(file) {
  let preview = document.querySelector('#preview');
  preview.innerHTML = "";
  if (!/\.(jpe?g|png|gif)$/i.test(file.name)) {
    return alert(file.name + " is not an image");
  }
  let reader = new FileReader();
  reader.addEventListener("load", function () {
    let image = new Image();
    image.height = 150; image.width = 150;
    image.title = file.name;
    image.alt = "img-" + file.name;
    image.src = this.result as string;

    preview.appendChild(image);
  });
  reader.readAsDataURL(file);
}

function previewImage(img: string) {
  let preview = document.querySelector('#preview');
  preview.innerHTML = "";

  let image = new Image();
  image.height = 150; image.width = 150;
  image.src = img;
  preview.appendChild(image);
}
