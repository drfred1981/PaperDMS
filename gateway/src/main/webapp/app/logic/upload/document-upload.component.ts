import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

/**
 * Component for uploading documents with drag-and-drop support.
 * Implements SHA-256 client-side hashing and upload progress tracking.
 */
@Component({
  selector: 'app-document-upload',
  templateUrl: './document-upload.component.html',
  styleUrls: ['./document-upload.component.scss']
})
export class DocumentUploadComponent implements OnInit {
  @ViewChild('fileInput', { static: false }) fileInput!: ElementRef<HTMLInputElement>;

  selectedFiles: File[] = [];
  uploadProgress: Map<string, number> = new Map();
  uploadStatus: Map<string, string> = new Map();
  isDragging = false;
  folderId: number | null = null;
  documentTypeId: number | null = null;
  documentTypes: any[] = [];
  folders: any[] = [];
  uploadSubscriptions: Map<string, Subscription> = new Map();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDocumentTypes();
    this.loadFolders();
  }

  /**
   * Load available document types from API.
   */
  loadDocumentTypes(): void {
    this.http.get<any[]>('/api/document-types').subscribe({
      next: (types) => {
        this.documentTypes = types;
        if (types.length > 0) {
          this.documentTypeId = types[0].id;
        }
      },
      error: (error) => {
        console.error('Failed to load document types', error);
      }
    });
  }

  /**
   * Load available folders from API.
   */
  loadFolders(): void {
    this.http.get<any[]>('/api/folders').subscribe({
      next: (folders) => {
        this.folders = folders;
      },
      error: (error) => {
        console.error('Failed to load folders', error);
      }
    });
  }

  /**
   * Handle drag over event.
   *
   * @param event The drag event
   */
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  /**
   * Handle drag leave event.
   *
   * @param event The drag event
   */
  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  /**
   * Handle file drop event.
   *
   * @param event The drag event
   */
  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;

    const files = event.dataTransfer?.files;
    if (files) {
      this.handleFiles(Array.from(files));
    }
  }

  /**
   * Handle file selection from input.
   *
   * @param event The input change event
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.handleFiles(Array.from(input.files));
    }
  }

  /**
   * Handle selected files.
   *
   * @param files Array of selected files
   */
  handleFiles(files: File[]): void {
    const pdfFiles = files.filter(file => file.type === 'application/pdf');
    
    if (pdfFiles.length !== files.length) {
      console.warn('Some files were rejected (only PDF files are accepted)');
    }

    this.selectedFiles = [...this.selectedFiles, ...pdfFiles];
    pdfFiles.forEach(file => {
      this.uploadStatus.set(file.name, 'pending');
      this.uploadProgress.set(file.name, 0);
    });
  }

  /**
   * Remove a file from the upload queue.
   *
   * @param file The file to remove
   */
  removeFile(file: File): void {
    const index = this.selectedFiles.indexOf(file);
    if (index > -1) {
      this.selectedFiles.splice(index, 1);
      this.uploadProgress.delete(file.name);
      this.uploadStatus.delete(file.name);
      
      const subscription = this.uploadSubscriptions.get(file.name);
      if (subscription) {
        subscription.unsubscribe();
        this.uploadSubscriptions.delete(file.name);
      }
    }
  }

  /**
   * Upload all selected files.
   */
  uploadAllFiles(): void {
    if (!this.documentTypeId) {
      console.error('Document type is required');
      return;
    }

    this.selectedFiles.forEach(file => {
      if (this.uploadStatus.get(file.name) === 'pending') {
        this.uploadFile(file);
      }
    });
  }

  /**
   * Upload a single file.
   *
   * @param file The file to upload
   */
  uploadFile(file: File): void {
    this.uploadStatus.set(file.name, 'hashing');
    
    this.calculateSha256(file).then(sha256 => {
      console.log(`SHA-256 calculated for ${file.name}: ${sha256}`);
      
      this.uploadStatus.set(file.name, 'uploading');
      
      const formData = new FormData();
      formData.append('file', file);
      if (this.folderId) {
        formData.append('folderId', this.folderId.toString());
      }
      formData.append('documentTypeId', this.documentTypeId!.toString());

      const uploadSubscription = this.http.post('/api/documents/upload', formData, {
        reportProgress: true,
        observe: 'events'
      }).pipe(
        finalize(() => {
          this.uploadSubscriptions.delete(file.name);
        })
      ).subscribe({
        next: (event) => {
          if (event.type === HttpEventType.UploadProgress) {
            const progress = event.total ? Math.round((100 * event.loaded) / event.total) : 0;
            this.uploadProgress.set(file.name, progress);
          } else if (event instanceof HttpResponse) {
            this.uploadProgress.set(file.name, 100);
            this.uploadStatus.set(file.name, 'completed');
            console.log(`Upload completed for ${file.name}`, event.body);
          }
        },
        error: (error) => {
          console.error(`Upload failed for ${file.name}`, error);
          this.uploadStatus.set(file.name, 'error');
          this.uploadProgress.set(file.name, 0);
        }
      });

      this.uploadSubscriptions.set(file.name, uploadSubscription);
    }).catch(error => {
      console.error(`SHA-256 calculation failed for ${file.name}`, error);
      this.uploadStatus.set(file.name, 'error');
    });
  }

  /**
   * Calculate SHA-256 hash of a file.
   *
   * @param file The file to hash
   * @return Promise resolving to the SHA-256 hash as hex string
   */
  private async calculateSha256(file: File): Promise<string> {
    const buffer = await file.arrayBuffer();
    const hashBuffer = await crypto.subtle.digest('SHA-256', buffer);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
  }

  /**
   * Clear all completed uploads.
   */
  clearCompleted(): void {
    this.selectedFiles = this.selectedFiles.filter(file => 
      this.uploadStatus.get(file.name) !== 'completed'
    );
    
    Array.from(this.uploadStatus.keys()).forEach(fileName => {
      if (this.uploadStatus.get(fileName) === 'completed') {
        this.uploadStatus.delete(fileName);
        this.uploadProgress.delete(fileName);
      }
    });
  }

  /**
   * Get status icon class.
   *
   * @param file The file
   * @return CSS class for status icon
   */
  getStatusIcon(file: File): string {
    const status = this.uploadStatus.get(file.name);
    switch (status) {
      case 'pending': return 'bi-clock';
      case 'hashing': return 'bi-calculator';
      case 'uploading': return 'bi-cloud-upload';
      case 'completed': return 'bi-check-circle-fill';
      case 'error': return 'bi-x-circle-fill';
      default: return 'bi-file-earmark';
    }
  }

  /**
   * Get status color class.
   *
   * @param file The file
   * @return CSS class for status color
   */
  getStatusColor(file: File): string {
    const status = this.uploadStatus.get(file.name);
    switch (status) {
      case 'completed': return 'text-success';
      case 'error': return 'text-danger';
      case 'uploading': return 'text-primary';
      default: return 'text-secondary';
    }
  }

  /**
   * Format file size for display.
   *
   * @param bytes File size in bytes
   * @return Formatted size string
   */
  formatFileSize(bytes: number): string {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / 1048576).toFixed(1) + ' MB';
  }
}
